# Code Examples for LibGDX 3D Model Editor

This document contains example code snippets that users can copy and paste into the editor, or use as reference when prompting AI chatbots.

## Example 1: Simple Cube

```java
// Create a simple colored cube
int cubeId = modelBuilder.box(1f, 1f, 1f);
Node cube = modelBuilder.node();
cube.id = "cube";
cube.translation.set(0f, 0.5f, 0f);
Material cubeMat = new Material(ColorAttribute.createDiffuse(new Color(0.8f, 0.2f, 0.2f, 1f)));
cube.parts.add(new Renderable(cubeId, cubeMat, null, null));
modelBuilder.addNode(cube, modelBuilder.end());
```

## Example 2: Low-Poly Tree

```java
// Tree trunk (brown cylinder)
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

// Ground plane
int groundId = modelBuilder.box(4f, 0.1f, 4f);
Node ground = modelBuilder.node();
ground.id = "ground";
ground.translation.set(0f, 0f, 0f);
Material groundMat = new Material(ColorAttribute.createDiffuse(new Color(0.3f, 0.5f, 0.3f, 1f)));
ground.parts.add(new Renderable(groundId, groundMat, null, null));
modelBuilder.addNode(ground, modelBuilder.end());
```

## Example 3: Simple House

```java
// House base (white cube)
int baseId = modelBuilder.box(2f, 2f, 2f);
Node base = modelBuilder.node();
base.id = "base";
base.translation.set(0f, 1f, 0f);
Material baseMat = new Material(ColorAttribute.createDiffuse(new Color(0.9f, 0.9f, 0.85f, 1f)));
base.parts.add(new Renderable(baseId, baseMat, null, null));
modelBuilder.addNode(base, modelBuilder.end());

// Roof (red pyramid/cone)
int roofId = modelBuilder.cone(1.6f, 1.5f, 1.6f, 4);
Node roof = modelBuilder.node();
roof.id = "roof";
roof.translation.set(0f, 2.75f, 0f);
roof.rotation.setFromAxisRad(new Vector3(0, 1, 0), (float)Math.PI / 4);
Material roofMat = new Material(ColorAttribute.createDiffuse(new Color(0.8f, 0.2f, 0.2f, 1f)));
roof.parts.add(new Renderable(roofId, roofMat, null, null));
modelBuilder.addNode(roof, modelBuilder.end());

// Door (brown box)
int doorId = modelBuilder.box(0.6f, 1.2f, 0.1f);
Node door = modelBuilder.node();
door.id = "door";
door.translation.set(0f, 0.6f, 1.05f);
Material doorMat = new Material(ColorAttribute.createDiffuse(new Color(0.4f, 0.25f, 0.1f, 1f)));
door.parts.add(new Renderable(doorId, doorMat, null, null));
modelBuilder.addNode(door, modelBuilder.end());
```

## Example 4: Robot Character

```java
// Body (gray box)
int bodyId = modelBuilder.box(0.8f, 1f, 0.5f);
Node body = modelBuilder.node();
body.id = "body";
body.translation.set(0f, 1.5f, 0f);
Material bodyMat = new Material(ColorAttribute.createDiffuse(new Color(0.6f, 0.6f, 0.65f, 1f)));
body.parts.add(new Renderable(bodyId, bodyMat, null, null));
modelBuilder.addNode(body, modelBuilder.end());

// Head (gray sphere)
int headId = modelBuilder.sphere(0.35f, 16, 16);
Node head = modelBuilder.node();
head.id = "head";
head.translation.set(0f, 2.2f, 0f);
Material headMat = new Material(ColorAttribute.createDiffuse(new Color(0.6f, 0.6f, 0.65f, 1f)));
head.parts.add(new Renderable(headId, headMat, null, null));
modelBuilder.addNode(head, modelBuilder.end());

// Left arm (cylinder)
int leftArmId = modelBuilder.cylinder(0.1f, 0.8f, 8, false);
Node leftArm = modelBuilder.node();
leftArm.id = "leftArm";
leftArm.translation.set(-0.55f, 1.6f, 0f);
leftArm.rotation.setFromAxisRad(new Vector3(1, 0, 0), (float)Math.PI / 6);
Material armMat = new Material(ColorAttribute.createDiffuse(new Color(0.5f, 0.5f, 0.55f, 1f)));
leftArm.parts.add(new Renderable(leftArmId, armMat, null, null));
modelBuilder.addNode(leftArm, modelBuilder.end());

// Right arm (cylinder)
Node rightArm = modelBuilder.node();
rightArm.id = "rightArm";
rightArm.translation.set(0.55f, 1.6f, 0f);
rightArm.rotation.setFromAxisRad(new Vector3(1, 0, 0), (float)Math.PI / 6);
rightArm.parts.add(new Renderable(leftArmId, armMat, null, null));
modelBuilder.addNode(rightArm, modelBuilder.end());

// Eyes (blue small spheres)
int eyeId = modelBuilder.sphere(0.08f, 8, 8);
Node leftEye = modelBuilder.node();
leftEye.id = "leftEye";
leftEye.translation.set(-0.12f, 2.25f, 0.3f);
Material eyeMat = new Material(ColorAttribute.createDiffuse(new Color(0.2f, 0.8f, 1f, 1f)));
leftEye.parts.add(new Renderable(eyeId, eyeMat, null, null));
modelBuilder.addNode(leftEye, modelBuilder.end());

Node rightEye = modelBuilder.node();
rightEye.id = "rightEye";
rightEye.translation.set(0.12f, 2.25f, 0.3f);
rightEye.parts.add(new Renderable(eyeId, eyeMat, null, null));
modelBuilder.addNode(rightEye, modelBuilder.end());
```

## Example 5: Sword

```java
// Blade (silver elongated diamond/cube)
int bladeId = modelBuilder.box(0.1f, 2.5f, 0.02f);
Node blade = modelBuilder.node();
blade.id = "blade";
blade.translation.set(0f, 1.5f, 0f);
Material bladeMat = new Material(ColorAttribute.createDiffuse(new Color(0.8f, 0.8f, 0.85f, 1f)));
blade.parts.add(new Renderable(bladeId, bladeMat, null, null));
modelBuilder.addNode(blade, modelBuilder.end());

// Guard (gold crossbar)
int guardId = modelBuilder.box(0.8f, 0.15f, 0.1f);
Node guard = modelBuilder.node();
guard.id = "guard";
guard.translation.set(0f, 0.4f, 0f);
Material guardMat = new Material(ColorAttribute.createDiffuse(new Color(0.9f, 0.7f, 0.2f, 1f)));
guard.parts.add(new Renderable(guardId, guardMat, null, null));
modelBuilder.addNode(guard, modelBuilder.end());

// Handle (brown cylinder)
int handleId = modelBuilder.cylinder(0.08f, 0.8f, 8, false);
Node handle = modelBuilder.node();
handle.id = "handle";
handle.translation.set(0f, 0f, 0f);
Material handleMat = new Material(ColorAttribute.createDiffuse(new Color(0.4f, 0.25f, 0.1f, 1f)));
handle.parts.add(new Renderable(handleId, handleMat, null, null));
modelBuilder.addNode(handle, modelBuilder.end());

// Pommel (gold sphere)
int pommelId = modelBuilder.sphere(0.12f, 12, 12);
Node pommel = modelBuilder.node();
pommel.id = "pommel";
pommel.translation.set(0f, -0.5f, 0f);
Material pommelMat = new Material(ColorAttribute.createDiffuse(new Color(0.9f, 0.7f, 0.2f, 1f)));
pommel.parts.add(new Renderable(pommelId, pommelMat, null, null));
modelBuilder.addNode(pommel, modelBuilder.end());
```

## Example 6: Ground with Rocks

```java
// Main ground plane
int groundId = modelBuilder.box(10f, 0.2f, 10f);
Node ground = modelBuilder.node();
ground.id = "ground";
ground.translation.set(0f, -0.1f, 0f);
Material groundMat = new Material(ColorAttribute.createDiffuse(new Color(0.4f, 0.55f, 0.3f, 1f)));
ground.parts.add(new Renderable(groundId, groundMat, null, null));
modelBuilder.addNode(ground, modelBuilder.end());

// Rock 1 (gray sphere, slightly flattened)
int rock1Id = modelBuilder.sphere(0.5f, 12, 12);
Node rock1 = modelBuilder.node();
rock1.id = "rock1";
rock1.translation.set(1.5f, 0.2f, 1f);
rock1.rotation.setFromAxisRad(new Vector3(1, 0, 0), (float)Math.PI / 6);
Material rockMat = new Material(ColorAttribute.createDiffuse(new Color(0.5f, 0.5f, 0.5f, 1f)));
rock1.parts.add(new Renderable(rock1Id, rockMat, null, null));
modelBuilder.addNode(rock1, modelBuilder.end());

// Rock 2 (smaller)
int rock2Id = modelBuilder.sphere(0.3f, 10, 10);
Node rock2 = modelBuilder.node();
rock2.id = "rock2";
rock2.translation.set(-2f, 0.15f, -1.5f);
rock2.parts.add(new Renderable(rock2Id, rockMat, null, null));
modelBuilder.addNode(rock2, modelBuilder.end());

// Rock 3 (medium)
Node rock3 = modelBuilder.node();
rock3.id = "rock3";
rock3.translation.set(0.5f, 0.25f, -2f);
rock3.rotation.setFromAxisRad(new Vector3(0, 0, 1), (float)Math.PI / 8);
rock3.parts.add(new Renderable(rock2Id, rockMat, null, null));
modelBuilder.addNode(rock3, modelBuilder.end());
```

## Color Reference

Here are some common colors you can use:

| Color | RGB Values |
|-------|------------|
| Red | (0.8f, 0.2f, 0.2f) |
| Green | (0.2f, 0.7f, 0.2f) |
| Blue | (0.2f, 0.3f, 0.8f) |
| Yellow | (0.9f, 0.8f, 0.1f) |
| Orange | (0.9f, 0.5f, 0.1f) |
| Brown | (0.4f, 0.25f, 0.1f) |
| White | (0.95f, 0.95f, 0.95f) |
| Gray | (0.5f, 0.5f, 0.5f) |
| Black | (0.1f, 0.1f, 0.1f) |
| Gold | (0.9f, 0.7f, 0.2f) |
| Silver | (0.8f, 0.8f, 0.85f) |

## Tips for Good Results

1. **Keep vertex count low**: Stay under 5000 vertices for smooth performance
2. **Use simple shapes**: Boxes, cylinders, cones, and spheres are easiest to work with
3. **Position carefully**: Use translation to move parts to the right places
4. **Combine similar materials**: Reuse material instances for efficiency
5. **Build incrementally**: Test each part before adding more
