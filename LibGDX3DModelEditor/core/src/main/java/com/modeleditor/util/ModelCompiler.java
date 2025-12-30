package com.modeleditor.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.Renderable;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ByteArrayClassLoader;
import org.codehaus.janino.ClassLoaderIClassLoader;
import org.codehaus.janino.IClassLoader;
import org.codehaus.janino.util.resource.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Compiler utility that uses Janino to dynamically compile and execute
 * Java code for creating LibGDX 3D models at runtime.
 */
public class ModelCompiler implements Disposable {

    private final Map<String, byte[]> classResources;
    private ClassLoaderIClassLoader iClassLoader;
    private ClassLoader classLoader;

    // Allowed classes that can be used in the compiled code
    private static final Map<String, Class<?>> ALLOWED_CLASSES = new HashMap<>();

    static {
        // LibGDX core classes
        ALLOWED_CLASSES.put("com.badlogic.gdx.graphics.g3d.ModelBuilder", ModelBuilder.class);
        ALLOWED_CLASSES.put("com.badlogic.gdx.graphics.g3d.Model", Model.class);
        ALLOWED_CLASSES.put("com.badlogic.gdx.graphics.g3d.model.Node", Node.class);
        ALLOWED_CLASSES.put("com.badlogic.gdx.graphics.g3d.model.Renderable", Renderable.class);
        ALLOWED_CLASSES.put("com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute", ColorAttribute.class);
        ALLOWED_CLASSES.put("com.badlogic.gdx.graphics.Color", Color.class);
        ALLOWED_CLASSES.put("com.badlogic.gdx.math.Vector3", Vector3.class);
        ALLOWED_CLASSES.put("com.badlogic.gdx.utils.Array", Array.class);
        ALLOWED_CLASSES.put("com.badlogic.gdx.utils.Array", Array.class);

        // Primitive wrapper classes
        ALLOWED_CLASSES.put("java.lang.String", String.class);
        ALLOWED_CLASSES.put("java.lang.Integer", Integer.class);
        ALLOWED_CLASSES.put("java.lang.Float", Float.class);
        ALLOWED_CLASSES.put("java.lang.Double", Double.class);
        ALLOWED_CLASSES.put("java.lang.Boolean", Boolean.class);
        ALLOWED_CLASSES.put("java.lang.Object", Object.class);
    }

    public ModelCompiler() {
        this.classResources = new HashMap<>();
        initializeClassLoader();
    }

    /**
     * Initialize the Janino class loader for dynamic compilation.
     */
    private void initializeClassLoader() {
        // Create a resource for the class we're going to compile
        classResources.put("com/modeleditor/runtime/ModelCreator.class", new byte[0]);

        // Create IClassLoader with allowed classes
        iClassLoader = new RestrictedIClassLoader(
            new IClassLoader[0],
            ALLOWED_CLASSES
        );

        // Create byte array class loader
        classLoader = new ByteArrayClassLoader(classResources, getClass().getClassLoader());
    }

    /**
     * Compile the given Java code and create a Model instance.
     * @param codeBody The Java code to compile (method body)
     * @return The created Model instance
     * @throws CompileException if compilation fails
     * @throws RuntimeException if model creation fails
     */
    public Model compileAndCreateModel(String codeBody) throws CompileException, RuntimeException {
        // Build the complete class code
        String fullClassCode = buildCompleteClass(codeBody);

        // Compile the class
        Class<?> creatorClass = compileClass(fullClassCode, "ModelCreator");

        // Create instance and call createModel
        try {
            Object creator = creatorClass.newInstance();
            Method createModelMethod = creatorClass.getMethod("createModel", ModelBuilder.class);
            ModelBuilder builder = new ModelBuilder();

            // Set up builder with common methods
            setupModelBuilderMethods(builder);

            Model result = (Model) createModelMethod.invoke(creator, builder);
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create model: " + e.getMessage(), e);
        }
    }

    /**
     * Build the complete Java class code from the method body.
     */
    private String buildCompleteClass(String codeBody) {
        StringBuilder sb = new StringBuilder();

        sb.append("package com.modeleditor.runtime;\n\n");
        sb.append("import com.badlogic.gdx.graphics.g3d.ModelBuilder;\n");
        sb.append("import com.badlogic.gdx.graphics.g3d.Model;\n");
        sb.append("import com.badlogic.gdx.graphics.g3d.model.Node;\n");
        sb.append("import com.badlogic.gdx.graphics.g3d.model.Renderable;\n");
        sb.append("import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;\n");
        sb.append("import com.badlogic.gdx.graphics.Color;\n");
        sb.append("import com.badlogic.gdx.math.Vector3;\n");
        sb.append("import com.badlogic.gdx.utils.Array;\n\n");

        sb.append("public class ModelCreator {\n\n");
        sb.append("    public Model createModel(ModelBuilder modelBuilder) {\n");
        sb.append("        // User code starts here\n");
        sb.append(codeBody);
        sb.append("\n        // User code ends here\n");
        sb.append("        return modelBuilder.end();\n");
        sb.append("    }\n\n");
        sb.append("}\n");

        return sb.toString();
    }

    /**
     * Compile a Java class from source code using Janino.
     */
    private Class<?> compileClass(String sourceCode, String className) throws CompileException {
        org.codehaus.janino.Compiler compiler = new org.codehaus.janino.Compiler(
            iClassLoader
        );

        // Create resource for the source
        Resource sourceResource = new Resource() {
            @Override
            public String getFileName() {
                return className + ".java";
            }

            @Override
            public InputStream open() throws IOException {
                return new ByteArrayInputStream(sourceCode.getBytes());
            }

            @Override
            public long lastModified() {
                return System.currentTimeMillis();
            }
        };

        // Compile the class
        compiler.compile(new Resource[]{sourceResource});

        // Get the compiled class
        try {
            return classLoader.loadClass("com.modeleditor.runtime." + className);
        } catch (ClassNotFoundException e) {
            throw new CompileException("Class not found after compilation", null);
        }
    }

    /**
     * Set up ModelBuilder with accessible methods for the compiled code.
     * This ensures the dynamically created code can use all ModelBuilder methods.
     */
    private void setupModelBuilderMethods(ModelBuilder builder) {
        // ModelBuilder already has all necessary methods exposed
        // The compiled code will have access to builder through the parameter
    }

    @Override
    public void dispose() {
        classResources.clear();
    }

    /**
     * Custom IClassLoader that only allows access to specific classes.
     * This provides security by restricting what classes can be used.
     */
    private static class RestrictedIClassLoader extends IClassLoader {

        private final Map<String, Class<?>> allowedClasses;

        public RestrictedIClassLoader(IClassLoader[] parent, Map<String, Class<?>> allowedClasses) {
            super(parent);
            this.allowedClasses = allowedClasses;
        }

        @Override
        public Class<?> findClass(String className) throws ClassNotFoundException {
            Class<?> allowedClass = allowedClasses.get(className);
            if (allowedClass != null) {
                return allowedClass;
            }
            throw new ClassNotFoundException("Class not allowed: " + className);
        }

        @Override
        public InputStream findResource(String resourceName) {
            return null;
        }
    }
}
