# AI Prompt Template

This is the exact text that gets copied to clipboard when users click the "Copy Prompt Template" button:

---

Generate ONLY pure Java code for LibGDX using ModelBuilder to create a low-poly, game-ready 3D model of: [DESCRIBE YOUR OBJECT HERE].

Return ONLY the body of this method — no class, no imports, no explanations:

```java
public static Model createModel() {
    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin();

    // Your code here using modelBuilder.part(), .box(), .cylinder(), .sphere(), .cone(), etc.
    // Use node().translate()/rotate() for positioning parts.
    // Use ColorAttribute.createDiffuse(new Color(r,g,b,1f)) for materials.
    // Keep it under 5000 vertices for performance.

    return modelBuilder.end();
}
```

---

## Usage Examples

### Example 1: Fantasy Sword
**User's input to AI:**
```
Generate ONLY pure Java code for LibGDX using ModelBuilder to create a low-poly, game-ready 3D model of: A fantasy sword with a silver blade, golden guard, and ruby in the pommel.

Return ONLY the body of this method — no class, no imports, no explanations:

public static Model createModel() {
    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin();

    // Your code here using modelBuilder.part(), .box(), .cylinder(), .sphere(), .cone(), etc.
    // Use node().translate()/rotate() for positioning parts.
    // Use ColorAttribute.createDiffuse(new Color(r,g,b,1f)) for materials.
    // Keep it under 5000 vertices for performance.

    return modelBuilder.end();
}
```

### Example 2: Wooden Crate
**User's input to AI:**
```
Generate ONLY pure Java code for LibGDX using ModelBuilder to create a low-poly, game-ready 3D model of: A simple wooden crate with visible planks.

Return ONLY the body of this method — no class, no imports, no explanations:

public static Model createModel() {
    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin();

    // Your code here using modelBuilder.part(), .box(), .cylinder(), .sphere(), .cone(), etc.
    // Use node().translate()/rotate() for positioning parts.
    // Use ColorAttribute.createDiffuse(new Color(r,g,b,1f)) for materials.
    // Keep it under 5000 vertices for performance.

    return modelBuilder.end();
}
```

### Example 3: Character
**User's input to AI:**
```
Generate ONLY pure Java code for LibGDX using ModelBuilder to create a low-poly, game-ready 3D model of: A simple robot character with boxy body, cylindrical arms, and sphere head.

Return ONLY the body of this method — no class, no imports, no explanations:

public static Model createModel() {
    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin();

    // Your code here using modelBuilder.part(), .box(), .cylinder(), .sphere(), .cone(), etc.
    // Use node().translate()/rotate() for positioning parts.
    // Use ColorAttribute.createDiffuse(new Color(r,g,b,1f)) for materials.
    // Keep it under 5000 vertices for performance.

    return modelBuilder.end();
}
```

## Tips for Getting Better Results

1. **Be Specific**: Describe materials, colors, and proportions
2. **Keep It Simple**: Start with basic shapes before complex models
3. **Mention Style**: "low-poly", "pixel art style", "minimalist"
4. **Include Scale**: Mention relative sizes ("larger than", "half the size")
5. **Specify Parts**: List all the components you want included
