package com.modeleditor.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.modeleditor.ui.EditorScreen;
import com.modeleditor.util.ModelCompiler;

/**
 * Main LibGDX application class for the 3D Model Editor.
 * This is a standalone on-device editor that allows users to
 * create procedural 3D models by pasting Java code generated from AI.
 */
public class ModelEditorApplication extends ApplicationAdapter {

    private EditorScreen editorScreen;
    private ModelCompiler modelCompiler;
    private Model currentModel;
    private ModelBatch modelBatch;
    private Environment environment;
    private CameraInputController cameraController;

    @Override
    public void create() {
        // Set application type for proper initialization
        Gdx.app.setLogLevel(Application.LOG_INFO);

        // Initialize the model compiler for runtime code compilation
        modelCompiler = new ModelCompiler();

        // Initialize the 3D rendering components
        initialize3DComponents();

        // Create and set the editor screen with split layout
        editorScreen = new EditorScreen(this);
        Gdx.input.setInputProcessor(editorScreen.getStage());

        // Load default example model
        loadDefaultModel();
    }

    /**
     * Initialize 3D rendering components including ModelBatch, camera, and lighting.
     */
    private void initialize3DComponents() {
        // Create model batch for rendering
        modelBatch = new ModelBatch();

        // Create environment with lighting
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.set(0.8f, 0.8f, 0.8f, -1f, -0.5f, -0.5f);
        environment.add(directionalLight);

        DirectionalLight backLight = new DirectionalLight();
        backLight.set(0.4f, 0.4f, 0.4f, 1f, 0.5f, 0.5f);
        environment.add(backLight);
    }

    /**
     * Load the default example model to demonstrate functionality.
     */
    private void loadDefaultModel() {
        String defaultCode = getDefaultExampleCode();
        compileAndLoadModel(defaultCode);
    }

    /**
     * Get the default example code that demonstrates basic model creation.
     */
    public static String getDefaultExampleCode() {
        return
            "// Create a simple low-poly tree\n" +
            "// This is an example of what you can create with ModelBuilder\n" +
            "\n" +
            "// Tree trunk (brown cylinder)\n" +
            "int trunkId = modelBuilder.cylinder(0.3f, 1.5f, 16, false);\n" +
            "Node trunk = modelBuilder.node();\n" +
            "trunk.id = \"trunk\";\n" +
            "trunk.translation.set(0f, 0.75f, 0f);\n" +
            "trunk.rotation.setFromAxisRad(new Vector3(1, 0, 0), 0f);\n" +
            "Material trunkMat = new Material(ColorAttribute.createDiffuse(new Color(0.4f, 0.25f, 0.1f, 1f)));\n" +
            "trunk.parts.add(new Renderable(trunkId, trunkMat, null, null));\n" +
            "modelBuilder.addNode(trunk, modelBuilder.end());\n" +
            "\n" +
            "// Tree foliage (green cone)\n" +
            "int foliageId = modelBuilder.cone(1.2f, 2f, 1.2f, 8);\n" +
            "Node foliage = modelBuilder.node();\n" +
            "foliage.id = \"foliage\";\n" +
            "foliage.translation.set(0f, 2.5f, 0f);\n" +
            "Material foliageMat = new Material(ColorAttribute.createDiffuse(new Color(0.2f, 0.6f, 0.2f, 1f)));\n" +
            "foliage.parts.add(new Renderable(foliageId, foliageMat, null, null));\n" +
            "modelBuilder.addNode(foliage, modelBuilder.end());\n" +
            "\n" +
            "// Ground plane\n" +
            "int groundId = modelBuilder.box(4f, 0.1f, 4f);\n" +
            "Node ground = modelBuilder.node();\n" +
            "ground.id = \"ground\";\n" +
            "ground.translation.set(0f, 0f, 0f);\n" +
            "Material groundMat = new Material(ColorAttribute.createDiffuse(new Color(0.3f, 0.5f, 0.3f, 1f)));\n" +
            "ground.parts.add(new Renderable(groundId, groundMat, null, null));\n" +
            "modelBuilder.addNode(ground, modelBuilder.end());";
    }

    /**
     * Compile the given Java code and load the resulting 3D model.
     * @param codeBody The Java code to compile (content of createModel method body)
     * @return true if compilation and model loading was successful
     */
    public boolean compileAndLoadModel(String codeBody) {
        try {
            // Dispose old model if exists
            if (currentModel != null) {
                currentModel.dispose();
                currentModel = null;
            }

            // Compile the code and create model
            currentModel = modelCompiler.compileAndCreateModel(codeBody);

            // Update camera controller with new model
            if (editorScreen != null) {
                editorScreen.onModelChanged(currentModel);
            }

            return true;

        } catch (Exception e) {
            Gdx.app.error("ModelEditor", "Failed to compile model: " + e.getMessage());
            if (editorScreen != null) {
                editorScreen.showError(e.getMessage());
            }
            return false;
        }
    }

    @Override
    public void render() {
        // Clear screen with dark background
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Render 3D model if available
        if (currentModel != null && editorScreen != null) {
            editorScreen.render3D(modelBatch, environment);
        }

        // Render UI
        if (editorScreen != null) {
            editorScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (editorScreen != null) {
            editorScreen.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        // Clean up resources
        if (modelBatch != null) {
            modelBatch.dispose();
            modelBatch = null;
        }
        if (currentModel != null) {
            currentModel.dispose();
            currentModel = null;
        }
        if (editorScreen != null) {
            editorScreen.dispose();
            editorScreen = null;
        }
        if (modelCompiler != null) {
            modelCompiler.dispose();
            modelCompiler = null;
        }
    }
}
