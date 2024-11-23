---
layout: default
title: Configuration
parent: Getting Started
nav_order: 4
---

# Configuration Guide
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Global Configuration

PDF Builder uses a central configuration object to manage document-wide settings.

### Basic Configuration

```java
PDFConfiguration config = PDFConfiguration.create()
    .withPageSize(PDRectangle.A4)
    .withMargins(50f, 40f, 50f, 40f)
    .withDPI(300)
    .build();

PDFBuilder builder = new PDFBuilder(config);
```

### Available Settings

| Setting | Description | Default | Example |
|---------|-------------|---------|---------|
| Page Size | Document page size | A4 | `PDRectangle.A4` |
| Margins | Document margins | 40f all sides | `new float[]{50f, 40f, 50f, 40f}` |
| DPI | Document resolution | 300 | `300` |
| Default Font | Base font | Helvetica | `"Times New Roman"` |
| Font Size | Base font size | 12f | `14f` |
| Line Spacing | Default line spacing | 1.2f | `1.5f` |
| Text Color | Default text color | Black | `Color.BLACK` |

## Page Configuration

### Page Size

Standard sizes:
```java
// Standard A4
.withPageSize(PDRectangle.A4)

// US Letter
.withPageSize(PDRectangle.LETTER)

// Custom size
.withPageSize(new PDRectangle(width, height))
```

### Margins

```java
// All margins equal
.withMargins(40f)

// Individual margins (top, right, bottom, left)
.withMargins(50f, 40f, 50f, 40f)

// Using MarginBuilder
.withMargins(Margin.builder()
    .withTop(50f)
    .withRight(40f)
    .withBottom(50f)
    .withLeft(40f)
    .build())
```

### Orientation

```java
// Portrait (default)
.withOrientation(Orientation.PORTRAIT)

// Landscape
.withOrientation(Orientation.LANDSCAPE)
```

## Font Configuration

### Default Font

```java
.withDefaultFont(Font.builder()
    .withName("Times New Roman")
    .withSize(12f)
    .withStyle(FontStyle.NORMAL)
    .build())
```

### Custom Fonts

```java
// Register custom font
.withCustomFont(Font.builder()
    .withName("MyCustomFont")
    .withPath("fonts/custom.ttf")
    .build())
```

### Font Collections

```java
// Define font collection
FontCollection fonts = FontCollection.builder()
    .addFont("heading", Font.builder()
        .withName("Arial")
        .withSize(16f)
        .withStyle(FontStyle.BOLD)
        .build())
    .addFont("body", Font.builder()
        .withName("Georgia")
        .withSize(12f)
        .build())
    .build();

// Use in configuration
.withFontCollection(fonts)
```

## Color Scheme

### Default Colors

```java
ColorScheme colors = ColorScheme.builder()
    .withPrimary(Color.BLACK)
    .withSecondary(Color.GRAY)
    .withAccent(Color.BLUE)
    .withBackground(Color.WHITE)
    .build();

.withColorScheme(colors)
```

### Component Colors

```java
// Table colors
TableStyle tableStyle = TableStyle.builder()
    .withHeaderBackground(colors.getPrimary())
    .withHeaderTextColor(Color.WHITE)
    .withBorderColor(colors.getSecondary())
    .build();
```

## Header and Footer

### Header Configuration

```java
HeaderConfig header = HeaderConfig.builder()
    .withHeight(50f)
    .withLogo(Logo.builder()
        .withText("Company Name")
        .withImage("logo.png")
        .build())
    .withShowOnFirstPage(true)
    .build();

.withHeader(header)
```

### Footer Configuration

```java
FooterConfig footer = FooterConfig.builder()
    .withHeight(40f)
    .withPageNumbering(PageNumbering.builder()
        .withFormat("Page {current} of {total}")
        .withAlignment(TextAlignment.CENTER)
        .build())
    .build();

.withFooter(footer)
```

## Advanced Settings

### Compression

```java
.withCompression(Compression.builder()
    .withImageCompression(0.8f)
    .withTextCompression(true)
    .build())
```

### Metadata

```java
.withMetadata(Metadata.builder()
    .withTitle("Document Title")
    .withAuthor("Author Name")
    .withKeywords("pdf, document")
    .withCreator("PDF Builder")
    .build())
```

### Security

```java
.withSecurity(Security.builder()
    .withUserPassword("user123")
    .withOwnerPassword("owner123")
    .withPermissions(Permission.PRINT, Permission.COPY)
    .build())
```

## Environment-Specific Configuration

### Development

```java
PDFConfiguration devConfig = PDFConfiguration.create()
    .withDebugMode(true)
    .withVerboseLogging(true)
    .withCacheEnabled(false)
    .build();
```

### Production

```java
PDFConfiguration prodConfig = PDFConfiguration.create()
    .withOptimization(Optimization.MAXIMUM)
    .withCacheEnabled(true)
    .withCompression(Compression.HIGH)
    .build();
```

## Best Practices

1. **Create Reusable Configurations**
   ```java
   public static PDFConfiguration getDefaultConfig() {
       return PDFConfiguration.create()
           .withPageSize(PDRectangle.A4)
           .withMargins(40f)
           .withDPI(300)
           .build();
   }
   ```

2. **Environment-Based Configuration**
   ```java
   PDFConfiguration config = isProd ? 
       ProductionConfig.get() : 
       DevelopmentConfig.get();
   ```

3. **Component-Specific Defaults**
   ```java
   .withDefaults(Defaults.builder()
       .withParagraphStyle(paragraphStyle)
       .withTableStyle(tableStyle)
       .withListStyle(listStyle)
       .build())
   ```

## Next Steps

- Learn about [Components](../components)
- See [Configuration Examples](../examples/configuration)
- Explore [Advanced Features](../advanced-features)
