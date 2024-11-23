---
layout: default
title: Basic Concepts
parent: Getting Started
nav_order: 2
---

# Basic Concepts
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Architecture Overview

PDF Builder is built on a component-based architecture that emphasizes:
- Type safety through builder patterns
- Immutable objects for thread safety
- Fluent interfaces for better readability
- Separation of concerns for maintainability

### Core Components

![Architecture Diagram](../assets/images/architecture.png)

1. **PDFBuilder**
   - Main entry point
   - Manages document lifecycle
   - Coordinates component rendering

2. **PDFConfiguration**
   - Document-level settings
   - Page setup
   - Global styling

3. **Components**
   - Self-contained PDF elements
   - Handle their own rendering
   - Maintain their state

## Key Concepts

### Builder Pattern

All components in PDF Builder use the builder pattern for several reasons:
- Type-safe construction
- Optional parameters
- Fluent interface
- Immutable objects

Example:
```java
Paragraph paragraph = Paragraph.builder()
    .addText("Hello")
    .withAlignment(TextAlignment.CENTER)
    .withFontSize(12f)
    .withColor(Color.BLACK)
    .build();
```

### Component Hierarchy

Components are organized in a hierarchical structure:

```
PDFBuilder
├── Page
│   ├── Header
│   │   ├── Logo
│   │   └── Text
│   ├── Content
│   │   ├── Paragraph
│   │   ├── Table
│   │   ├── List
│   │   └── Image
│   └── Footer
│       ├── PageNumber
│       └── Text
└── Metadata
```

### Coordinate System

PDF Builder uses a coordinate system where:
- Origin (0,0) is at the top-left corner
- X increases to the right
- Y increases downward
- Units are in points (1/72 inch)

```java
Coordinates pos = Coordinates.builder()
    .withX(100f)  // 100 points from left
    .withY(50f)   // 50 points from top
    .build();
```

### Safe Areas

To ensure content is properly positioned:
- Margins define the outer boundary
- Safe areas prevent content overflow
- Headers and footers have reserved spaces

```java
SafeArea safeArea = SafeArea.builder()
    .withTop(50f)
    .withRight(40f)
    .withBottom(50f)
    .withLeft(40f)
    .build();
```

## Component Types

### Text Components

1. **Paragraph**
   - Basic text container
   - Supports multiple styles
   - Automatic word wrapping

2. **Heading**
   - Six levels (H1-H6)
   - Consistent styling
   - Automatic spacing

3. **List**
   - Ordered and unordered
   - Nested lists
   - Custom markers

### Layout Components

1. **Table**
   - Row and column management
   - Cell spanning
   - Border control
   - Background colors

2. **Grid**
   - Flexible layouts
   - Column-based
   - Responsive sizing

### Media Components

1. **Image**
   - Multiple formats
   - Automatic scaling
   - Position control
   - Captions

2. **Logo**
   - Special image handling
   - Consistent placement
   - Size constraints

## Best Practices

1. **Configuration**
   - Set global styles in PDFConfiguration
   - Use consistent margins
   - Define standard fonts

2. **Component Usage**
   - Prefer builders over constructors
   - Chain method calls for readability
   - Use appropriate component types

3. **Resource Management**
   - Close resources properly
   - Use try-with-resources
   - Handle large files carefully

4. **Error Handling**
   - Catch specific exceptions
   - Provide meaningful error messages
   - Log issues appropriately

## Next Steps

- Follow the [Quick Start Guide](quick-start)
- Learn about [Configuration Options](configuration)
- See [Component Examples](../components)
