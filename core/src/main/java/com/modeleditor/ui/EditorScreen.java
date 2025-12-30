package com.modeleditor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.modeleditor.core.ModelEditorApplication;

/**
 * Main editor screen with split layout:
 * - Left side: Code input area (60% width)
 * - Right side: 3D preview viewport (40% width)
 */
public class EditorScreen {

    private final ModelEditorApplication application;
    private final Stage stage;
    private final Viewport viewport;

    // UI Components
    private TextField codeTextField;
    private Label errorLabel;
    private Label statusLabel;
    private Button compileButton;
    private Button copyPromptButton;
    private Button exampleButton;

    // 3D Preview Components
    private PerspectiveCamera camera;
    private CameraInputController cameraController;
    private Model currentModel;
    private final float viewportWidth;
    private final float viewportHeight;

    public EditorScreen(ModelEditorApplication application) {
        this.application = application;
        this.viewportWidth = Gdx.graphics.getWidth();
        this.viewportHeight = Gdx.graphics.getHeight();

        // Create stage with fit viewport
        viewport = new FitViewport(viewportWidth, viewportHeight);
        stage = new Stage(viewport);

        // Initialize 3D camera
        initializeCamera();

        // Build the UI
        buildUI();

        // Add the stage to the input processor
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Initialize the perspective camera for 3D preview.
     */
    private void initializeCamera() {
        float aspectRatio = (viewportWidth * 0.4f) / viewportHeight;
        camera = new PerspectiveCamera(67f, aspectRatio, 0.1f, 1000f);
        camera.position.set(0, 3, 8);
        camera.lookAt(0, 0, 0);

        // Create camera input controller for orbit controls
        cameraController = new CameraInputController(camera);
        cameraController.target.set(0, 0, 0);
    }

    /**
     * Create a programmatically defined skin for UI components.
     */
    private Skin createSkin() {
        Skin skin = new Skin();

        // Create font
        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        // Define colors
        Color white = Color.WHITE;
        Color black = Color.BLACK;
        Color darkBg = new Color(0.15f, 0.15f, 0.2f, 1f);
        Color buttonColor = new Color(0.3f, 0.5f, 0.7f, 1f);
        Color buttonDownColor = new Color(0.4f, 0.6f, 0.8f, 1f);
        Color textFieldBg = new Color(0.1f, 0.1f, 0.15f, 1f);
        Color selectionColor = new Color(0.3f, 0.5f, 0.7f, 0.5f);
        Color cursorColor = new Color(0.8f, 0.8f, 0.9f, 1f);

        // Create textures for buttons (simple solid color rectangles)
        Texture buttonTexture = createSolidTexture(buttonColor, 4, 4);
        Texture buttonDownTexture = createSolidTexture(buttonDownColor, 4, 4);
        Texture textFieldTexture = createSolidTexture(textFieldBg, 4, 4);

        // Create drawables
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        TextureRegionDrawable buttonDownDrawable = new TextureRegionDrawable(new TextureRegion(buttonDownTexture));
        TextureRegionDrawable textFieldDrawable = new TextureRegionDrawable(new TextureRegion(textFieldTexture));
        TextureRegionDrawable selectionDrawable = new TextureRegionDrawable(new TextureRegion(createSolidTexture(selectionColor, 2, 2)));
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(createSolidTexture(cursorColor, 2, 2)));

        // TextButton style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = buttonDrawable;
        textButtonStyle.down = buttonDownDrawable;
        textButtonStyle.font = font;
        textButtonStyle.fontColor = white;
        skin.add("default", textButtonStyle, TextButton.TextButtonStyle.class);

        // TextField style
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = textFieldDrawable;
        textFieldStyle.selection = selectionDrawable;
        textFieldStyle.cursor = cursorDrawable;
        textFieldStyle.font = font;
        textFieldStyle.fontColor = white;
        skin.add("default", textFieldStyle, TextField.TextFieldStyle.class);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = white;
        skin.add("default", labelStyle, Label.LabelStyle.class);

        // ScrollPane style
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = textFieldDrawable;
        skin.add("default", scrollPaneStyle, ScrollPane.ScrollPaneStyle.class);

        return skin;
    }

    /**
     * Create a simple solid color texture.
     */
    private Texture createSolidTexture(Color color, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /**
     * Build the complete user interface with split layout.
     */
    private void buildUI() {
        // Create main table for split layout
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Create skin for UI
        Skin skin = createSkin();

        // Calculate split positions
        float leftWidth = viewportWidth * 0.6f;
        float rightWidth = viewportWidth * 0.4f;

        // LEFT PANEL - Code Input Area
        Table leftPanel = new Table();
        leftPanel.pad(10);

        // Top section with buttons
        Table buttonRow = new Table();
        copyPromptButton = new TextButton("Copy Prompt Template", skin);
        copyPromptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                copyPromptTemplate();
            }
        });

        exampleButton = new TextButton("Load Example", skin);
        exampleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadExampleCode();
            }
        });

        buttonRow.add(copyPromptButton).padRight(10).height(45);
        buttonRow.add(exampleButton).height(45);

        // Code text input
        Label codeLabel = new Label("Paste Java Code Below:", skin);

        codeTextField = new TextField(getDefaultCode(), skin);
        codeTextField.setAlignment(Align.top | Align.left);

        // Create a scrollable text area using ScrollPane
        ScrollPane codeScrollPane = new ScrollPane(codeTextField, skin);
        codeScrollPane.setFadeScrollBars(false);

        // Error label (initially hidden)
        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setWrap(true);
        errorLabel.setAlignment(Align.topLeft);

        // Status label
        statusLabel = new Label("Ready. Click 'Compile & Preview' to render your model.", skin);
        statusLabel.setColor(Color.GREEN);

        // Compile button
        compileButton = new TextButton("Compile & Preview", skin);
        compileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                compileAndPreview();
            }
        });

        // Assemble left panel
        leftPanel.add(buttonRow).fillX().padBottom(15);
        leftPanel.row();
        leftPanel.add(codeLabel).padBottom(5).align(Align.left);
        leftPanel.row();
        leftPanel.add(codeScrollPane).fill().padBottom(15).minHeight(300);
        leftPanel.row();
        leftPanel.add(compileButton).fillX().height(50).padBottom(15);
        leftPanel.row();
        leftPanel.add(errorLabel).fillX().height(80).visible(false).align(Align.topLeft);
        leftPanel.row();
        leftPanel.add(statusLabel).fillX().align(Align.center);

        // RIGHT PANEL - 3D Preview
        Table rightPanel = new Table();

        Label previewLabel = new Label("3D Preview\n(Drag to rotate, Scroll to zoom)", skin);
        previewLabel.setAlignment(Align.center);

        rightPanel.add(previewLabel).align(Align.top).padBottom(10);

        // Add panels to main table
        mainTable.add(leftPanel).width(leftWidth).top().pad(5);
        mainTable.add(rightPanel).width(rightWidth).top().pad(5).fill().expand();
    }

    /**
     * Get the default example code.
     */
    private String getDefaultCode() {
        return
            "// Create a simple low-poly tree\n" +
            "// This is an example of what you can create with ModelBuilder\n" +
            "\n" +
            "// Tree trunk (brown cylinder)\n" +
            "int trunkId = modelBuilder.cylinder(0.3f, 1.5f, 16, false);\n" +
            "Node trunk = modelBuilder.node();\n" +
            "trunk.id = \"trunk\";\n" +
            "trunk.translation.set(0f, 0.75f, 0f);\n" +
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
     * Copy the AI prompt template to clipboard.
     */
    private void copyPromptTemplate() {
        String promptTemplate =
            "Generate ONLY pure Java code for LibGDX using ModelBuilder to create a low-poly, game-ready 3D model of: [DESCRIBE YOUR OBJECT HERE].\n\n" +
            "Return ONLY the body of this method — no class, no imports, no explanations:\n\n" +
            "public static Model createModel() {\n" +
            "    ModelBuilder modelBuilder = new ModelBuilder();\n" +
            "    modelBuilder.begin();\n\n" +
            "    // Your code here using modelBuilder.part(), .box(), .cylinder(), .sphere(), .cone(), etc.\n" +
            "    // Use node().translate()/rotate() for positioning parts.\n" +
            "    // Use ColorAttribute.createDiffuse(new Color(r,g,b,1f)) for materials.\n" +
            "    // Keep it under 5000 vertices for performance.\n\n" +
            "    return modelBuilder.end();\n" +
            "}";

        Clipboard clipboard = Gdx.app.getClipboard();
        clipboard.setContents(promptTemplate);

        statusLabel.setText("Prompt template copied to clipboard!");
        statusLabel.setColor(Color.GREEN);
    }

    /**
     * Load example code into the text area.
     */
    private void loadExampleCode() {
        codeTextField.setText(getDefaultCode());
        statusLabel.setText("Example code loaded. Click 'Compile & Preview' to render.");
        statusLabel.setColor(Color.GREEN);
    }

    /**
     * Compile the code and preview the 3D model.
     */
    private void compileAndPreview() {
        String code = codeTextField.getText();
        if (code.trim().isEmpty()) {
            showError("Please enter some Java code to compile.");
            return;
        }

        statusLabel.setText("Compiling...");
        statusLabel.setColor(Color.YELLOW);

        boolean success = application.compileAndLoadModel(code);

        if (success) {
            statusLabel.setText("Model compiled successfully!");
            statusLabel.setColor(Color.GREEN);
            errorLabel.setVisible(false);
        } else {
            statusLabel.setText("Compilation failed.");
            statusLabel.setColor(Color.RED);
        }
    }

    /**
     * Show an error message in the UI.
     */
    public void showError(String errorMessage) {
        errorLabel.setText("Error:\n" + errorMessage);
        errorLabel.setVisible(true);
        errorLabel.setColor(Color.RED);
    }

    /**
     * Called when the model is successfully changed.
     */
    public void onModelChanged(Model model) {
        this.currentModel = model;
    }

    /**
     * Render the 3D model in the preview viewport.
     */
    public void render3D(ModelBatch modelBatch, Environment environment) {
        if (currentModel == null) return;

        // Calculate the 3D viewport bounds
        float rightPanelStart = viewportWidth * 0.6f;
        float rightPanelWidth = viewportWidth * 0.4f;

        // Set viewport for 3D rendering
        Gdx.gl.glViewport((int) rightPanelStart, 0, (int) rightPanelWidth, (int) viewportHeight);

        // Update camera
        cameraController.update();

        // Render model
        modelBatch.begin(camera);
        modelBatch.render(currentModel, environment);
        modelBatch.end();

        // Reset viewport for UI rendering
        Gdx.gl.glViewport(0, 0, (int) viewportWidth, (int) viewportHeight);
    }

    /**
     * Render the UI stage.
     */
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    /**
     * Handle window resize.
     */
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Get the stage for input processing.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Dispose of resources.
     */
    public void dispose() {
        stage.dispose();
    }
}
