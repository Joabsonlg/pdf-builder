---
layout: default
title: List
parent: Text Components
grand_parent: Components
nav_order: 3
---

# List
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

The List component allows you to create ordered and unordered lists in your PDF documents. It supports multiple levels of nesting, custom markers, and rich formatting options.

## Basic Usage

```java
List list = List.builder()
    .withType(ListType.UNORDERED)
    .addItem("First item")
    .addItem("Second item")
    .addItem("Third item")
    .build();
```

## Features

### List Types

Choose between ordered and unordered lists:

```java
// Unordered list (bullet points)
List unorderedList = List.builder()
    .withType(ListType.UNORDERED)
    .addItem("Bullet point 1")
    .addItem("Bullet point 2")
    .build();

// Ordered list (numbers)
List orderedList = List.builder()
    .withType(ListType.ORDERED)
    .addItem("Step 1")
    .addItem("Step 2")
    .build();
```

### Marker Styles

Customize list markers:

```java
// Unordered list markers
List unorderedList = List.builder()
    .withType(ListType.UNORDERED)
    .withMarkerStyle(MarkerStyle.builder()
        .withType(MarkerType.BULLET)  // BULLET, DASH, SQUARE, CIRCLE
        .withColor(Color.BLUE)
        .withSize(4f)
        .build())
    .addItem("Custom bullet item")
    .build();

// Ordered list markers
List orderedList = List.builder()
    .withType(ListType.ORDERED)
    .withMarkerStyle(MarkerStyle.builder()
        .withType(MarkerType.DECIMAL)  // DECIMAL, ROMAN, ALPHA
        .withFormat("%d)")  // "1)", "2)", etc.
        .withColor(Color.RED)
        .build())
    .addItem("Custom numbered item")
    .build();
```

### Nested Lists

Create hierarchical lists:

```java
List nestedList = List.builder()
    .withType(ListType.UNORDERED)
    .addItem("Main item 1")
    .addSubList(List.builder()
        .withType(ListType.ORDERED)
        .addItem("Sub item 1.1")
        .addItem("Sub item 1.2")
        .build())
    .addItem("Main item 2")
    .build();
```

### Item Styling

Apply styles to list items:

```java
List styledList = List.builder()
    .withType(ListType.UNORDERED)
    .addItem(ListItem.builder()
        .withText("Styled item")
        .withFont(Font.builder()
            .withName("Arial")
            .withSize(12f)
            .withStyle(FontStyle.BOLD)
            .build())
        .withColor(Color.BLUE)
        .build())
    .build();
```

### Spacing Control

Adjust list and item spacing:

```java
List spacedList = List.builder()
    .withType(ListType.UNORDERED)
    .withSpacingBefore(10f)
    .withSpacingAfter(10f)
    .withItemSpacing(5f)
    .addItem("Spaced item 1")
    .addItem("Spaced item 2")
    .build();
```

## Advanced Features

### Custom Markers

Create custom list markers:

```java
List customList = List.builder()
    .withType(ListType.UNORDERED)
    .withMarkerStyle(MarkerStyle.builder()
        .withType(MarkerType.CUSTOM)
        .withCustomMarker("→")
        .withFont(Font.builder()
            .withName("Arial")
            .withSize(12f)
            .build())
        .withColor(Color.BLUE)
        .build())
    .addItem("Custom marker item")
    .build();
```

### Continuation

Continue numbering from previous lists:

```java
List firstList = List.builder()
    .withType(ListType.ORDERED)
    .addItem("First list item 1")
    .addItem("First list item 2")
    .build();

List continuedList = List.builder()
    .withType(ListType.ORDERED)
    .withStartNumber(3)  // Continue from previous list
    .addItem("Continued list item 3")
    .addItem("Continued list item 4")
    .build();
```

### Mixed Content

Combine text styles within list items:

```java
List mixedList = List.builder()
    .withType(ListType.UNORDERED)
    .addItem(ListItem.builder()
        .addText("Regular text with ")
        .addBoldText("bold")
        .addText(" and ")
        .addItalicText("italic")
        .addText(" parts")
        .build())
    .build();
```

## Complex Examples

### Documentation Style List

```java
List docList = List.builder()
    .withType(ListType.ORDERED)
    .withMarkerStyle(MarkerStyle.builder()
        .withType(MarkerType.DECIMAL)
        .withFormat("%d. ")
        .withColor(Color.rgb(0, 51, 102))
        .build())
    .addItem(ListItem.builder()
        .addBoldText("Installation: ")
        .addText("Download and install the package")
        .build())
    .addSubList(List.builder()
        .withType(ListType.UNORDERED)
        .withMarkerStyle(MarkerStyle.builder()
            .withType(MarkerType.BULLET)
            .withColor(Color.GRAY)
            .build())
        .addItem("Download from repository")
        .addItem("Run installation script")
        .build())
    .addItem(ListItem.builder()
        .addBoldText("Configuration: ")
        .addText("Set up your environment")
        .build())
    .withFont(Font.builder()
        .withName("Arial")
        .withSize(12f)
        .build())
    .withItemSpacing(8f)
    .build();
```

### Task List

```java
List taskList = List.builder()
    .withType(ListType.UNORDERED)
    .withMarkerStyle(MarkerStyle.builder()
        .withType(MarkerType.CUSTOM)
        .withCustomMarker("☐")
        .withFont(Font.builder()
            .withName("Arial Unicode MS")
            .withSize(12f)
            .build())
        .build())
    .addItem(ListItem.builder()
        .addText("Complete documentation")
        .withColor(Color.GRAY)
        .build())
    .addItem(ListItem.builder()
        .addText("Review code")
        .withColor(Color.BLACK)
        .build())
    .addItem(ListItem.builder()
        .addText("Submit changes")
        .withColor(Color.BLACK)
        .build())
    .withItemSpacing(5f)
    .build();
```

### Multi-level Outline

```java
List outline = List.builder()
    .withType(ListType.ORDERED)
    .withMarkerStyle(MarkerStyle.builder()
        .withType(MarkerType.ROMAN)
        .withCase(Case.UPPER)
        .build())
    .addItem("Main Topic")
    .addSubList(List.builder()
        .withType(ListType.ORDERED)
        .withMarkerStyle(MarkerStyle.builder()
            .withType(MarkerType.ALPHA)
            .withCase(Case.LOWER)
            .build())
        .addItem("Subtopic")
        .addSubList(List.builder()
            .withType(ListType.ORDERED)
            .withMarkerStyle(MarkerStyle.builder()
                .withType(MarkerType.DECIMAL)
                .build())
            .addItem("Detail point")
            .build())
        .build())
    .build();
```

## Best Practices

1. **List Structure**
   - Keep lists focused and concise
   - Use appropriate list types
   - Maintain consistent formatting

2. **Nesting**
   - Limit nesting to 3-4 levels
   - Use clear hierarchy
   - Consider readability

3. **Styling**
   - Use consistent marker styles
   - Maintain adequate spacing
   - Ensure marker-text alignment

4. **Content**
   - Keep items parallel in structure
   - Use consistent punctuation
   - Balance item lengths

## Common Issues

### Marker Alignment

Problem: Markers not aligning properly.

Solution:
```java
List alignedList = List.builder()
    .withType(ListType.ORDERED)
    .withMarkerAlignment(MarkerAlignment.TOP)
    .withMarkerOffset(10f)
    .build();
```

### Long Items

Problem: Items wrapping incorrectly.

Solution:
```java
List wrappingList = List.builder()
    .withType(ListType.UNORDERED)
    .withTextWrap(true)
    .withWrapIndent(20f)
    .build();
```

### Mixed Languages

Problem: Markers not displaying correctly with different scripts.

Solution:
```java
List multiLangList = List.builder()
    .withType(ListType.UNORDERED)
    .withMarkerStyle(MarkerStyle.builder()
        .withFont(Font.builder()
            .withName("Arial Unicode MS")
            .build())
        .build())
    .build();
```

## Next Steps

- Learn about [Paragraph](paragraph) components
- Explore [Heading](heading) components
- See [Text](text) component for inline styling
- Check out [Layout Components](../layout) for structural elements
