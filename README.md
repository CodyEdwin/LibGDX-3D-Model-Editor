# LibGDX 3D Model Editor

A complete, standalone, fully on-device 3D model editor application for LibGDX that allows users to create procedural 3D models by pasting Java code generated from any external AI (like ChatGPT, Grok, Claude, etc.).

## Overview

This editor provides a split-screen interface where users can:
- **Left Panel**: Paste Java code using LibGDX's ModelBuilder API
- **Right Panel**: Instantly preview the compiled 3D model with orbit controls

The application uses Janino for runtime Java code compilation, making it 100% on-device with no internet connection required.

## Features

### Main Screen Features

1. **Split Layout (Scene2D.ui)**
   - Left side (60% width): Large scrollable TextArea for pasting Java code
   - Right side (40% width): 3D preview viewport with orbit controls
   - Drag to rotate, scroll to zoom

2. **Code Pasting & Compilation**
   - Big multiline TextArea pre-filled with a placeholder example method
   - "Compile & Preview" button
   - When clicked:
     - Extracts the code inside the createModel() method body
     - Uses Janino to dynamically compile and execute it
     - Creates a live Model instance
     - Displays it in the 3D view using ModelBatch, PerspectiveCamera, and basic directional lighting + ambient light
   - Shows clear error messages (compilation or runtime exceptions)

3. **Ready-to-Copy Prompt Template**
   - A prominent "Copy Prompt Template" button at the top
   - Copies a ready-to-use prompt for AI chatbots to the clipboard

## How to Use

### Step 1: Get a Prompt Template
Click the "Copy Prompt Template" button to copy a ready-to-use prompt to your clipboard.

### Step 2: Generate Code from AI
Paste the template into your preferred AI chatbot (ChatGPT, Grok, Claude, etc.) and describe the object you want to create, for example:
- "A low-poly fantasy sword with ruby gem"
- "A simple wooden crate"
- "A low-poly tree with brown trunk and green foliage"

### Step 3: Paste and Compile
1. Copy the Java code returned by the AI
2. Paste it into the text area in the left panel
3. Click "Compile & Preview"
4. Your 3D model will appear in the right panel!

## Project Structure

```
LibGDX3DModelEditor/
├── core/                          # Core LibGDX code
│   └── src/main/java/
│       └── com/modeleditor/
│           ├── core/
│           │   └── ModelEditorApplication.java    # Main application
│           ├── ui/
│           │   └── EditorScreen.java              # UI with split layout
│           └── util/
│               └── ModelCompiler.java             # Janino runtime compilation
├── desktop/                       # Desktop launcher
│   └── src/main/java/
│       └── com/modeleditor/desktop/
│           └── DesktopLauncher.java
├── android/                       # Android launcher
│   └── src/main/java/
│       └── com/modeleditor/android/
│           └── AndroidLauncher.java
├── build.gradle                   # Gradle build configuration
├── settings.gradle                # Gradle settings
└── README.md                      # This file
```

## Building the Project

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Gradle 8.4 or higher
- Android SDK (for Android build)

### Building Desktop Version

```bash
cd LibGDX3DModelEditor
./gradlew desktop:run
```

Or build the JAR:

```bash
./gradlew desktop:dist
```

### Building Android Version

```bash
./gradlew android:build
```

## Code Example

Here's what the AI-generated code looks like:

```java
// Create a simple low-poly tree
int trunkId = modelBuilder.cylinder(0.3f, 1.5f, 16, false);
Node trunk = modelBuilder.node();
trunk.id = "trunk";
trunk.translation.set(0f, 0.75f, 0f);
Material trunkMat = new Material(ColorAttribute.createDiffuse(new Color(0.4f, 0.25f, 0.1f, 1f)));
trunk.parts.add(new Renderable(trunkId, trunkMat, null, null));
modelBuilder.addNode(trunk, modelBuilder.end());

// Tree foliage (green cone)
int foliageId = modelBuilder.cone(1.2f, 2f, 1.2f, 8);
Node foliage = modelBuilder.node();
foliage.id = "foliage";
foliage.translation.set(0f, 2.5f, 0f);
Material foliageMat = new Material(ColorAttribute.createDiffuse(new Color(0.2f, 0.6f, 0.2f, 1f)));
foliage.parts.add(new Renderable(foliageId, foliageMat, null, null));
modelBuilder.addNode(foliage, modelBuilder.end());
```

## API Reference

### Available LibGDX Classes

The following classes are available for use in your generated code:

- `ModelBuilder` - Main class for building 3D models
- `Model` - The resulting 3D model
- `Node` - A node in the model hierarchy
- `Renderable` - A renderable part of a node
- `ColorAttribute` - Material color attribute
- `Color` - Color representation
- `Vector3` - 3D vector
- `Array` - LibGDX array class

### Supported Primitives

You can create shapes using:
- `modelBuilder.box(width, height, depth)`
- `modelBuilder.cylinder(radius, height, segments, closed)`
- `modelBuilder.sphere(radius, divisions, subdivisions)`
- `modelBuilder.cone(radius, height, segments)`

## Dependencies

- **LibGDX 1.12.1**: Main game framework
- **Janino 3.1.6**: Runtime Java code compiler
- **LWJGL 3**: Desktop OpenGL bindings

## License

This project is open source and available for personal and commercial use.

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.

## Support

For questions or issues, please open a GitHub issue.
