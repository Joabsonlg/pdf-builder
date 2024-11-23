# Contributing Guide

Thank you for considering contributing to PDF Builder! This document provides guidelines for contributing to the project.

## Getting Started

1. Fork the repository
2. Clone your fork (`git clone git@github.com:your-username/pdf-builder.git`)
3. Create your feature branch (`git checkout -b feature/AmazingFeature`)
4. Commit your changes (`git commit -m 'feat: add some amazing feature'`)
5. Push to the branch (`git push origin feature/AmazingFeature`)
6. Open a Pull Request

## Git Workflow

### Branches
- `main`: Production code
- `develop`: Development code for next release
- `feature/*`: New features
- `bugfix/*`: Bug fixes
- `release/*`: Release preparation

### Development Process
1. **New Feature**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/feature-name
   # Develop your feature
   git commit -m "feat: description of the feature"
   ```

2. **Bug Fix**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b bugfix/bug-description
   # Fix the bug
   git commit -m "fix: description of the fix"
   ```

3. **Commits**
   We follow Conventional Commits:
   - `feat:` New feature
   - `fix:` Bug fix
   - `docs:` Documentation changes
   - `test:` Adding/modifying tests
   - `refactor:` Code refactoring
   - `style:` Formatting, semicolons, etc
   - `chore:` Build updates, dependencies, etc

4. **Pull Requests**
   - Create PR from your branch to `develop`
   - Wait for code review
   - Ensure all checks pass:
     - Build
     - Tests
     - Coverage (min 80%)
     - Checkstyle
     - SpotBugs

5. **Release**
   ```bash
   git checkout develop
   git checkout -b release/v1.x.x
   # Update version in pom.xml
   git commit -m "chore: bump version to v1.x.x"
   # After testing and approval
   git checkout main
   git merge release/v1.x.x
   git tag v1.x.x
   ```

## Code Quality

### Before Submitting a PR
1. Run tests: `mvn test`
2. Check coverage: `mvn jacoco:report`
3. Run checkstyle: `mvn checkstyle:check`
4. Run spotbugs: `mvn spotbugs:check`
5. Format your code
6. Update documentation if needed

### Code Standards
- Follow Google Java Style Guide
- Keep methods small and focused
- Write tests for new code
- Document public APIs with JavaDoc
- Maintain test coverage above 80%

## Development Requirements

### Java
- Use Java 17 or higher
- Follow Java best practices
- Keep dependencies up to date

### Testing
- Write unit tests for new features
- Include integration tests when needed
- Follow test naming convention: `given_when_then`
- Mock external dependencies

### Documentation
- Update README.md for new features
- Keep JavaDoc up to date
- Include code examples
- Document breaking changes

## Development Environment Setup

### Git Hooks

The project uses Git hooks to maintain code quality. These are automatically installed by Maven when you build the project.

The hooks enforce our quality standards:

**Pre-commit:**
- Code style (Checkstyle)
- Compilation check

**Pre-push:**
- Unit tests
- Test coverage (minimum 80%)
- Static analysis (SpotBugs)

## Issues

- Check existing issues before creating a new one
- Use appropriate labels
- Clearly describe the problem or feature
- Include steps to reproduce bugs

## Branches

- `main`: Always stable
- `develop`: Development branch
- `feature/*`: New features
- `fix/*`: Bug fixes
- `docs/*`: Documentation updates

## Pull Requests

- Clearly describe changes
- Reference related issues
- Wait for review before merging
- Keep PRs small and focused

## Running Tests

```bash
# Run all tests
mvn clean test

# Run tests for a specific class
mvn test -Dtest=ClassNameTest

# Run a specific test
mvn test -Dtest=ClassNameTest#methodName
```

## Documentation

- Keep README.md up to date
- Document new features
- Update examples when necessary
- Include comments in complex code

## Questions?

If you have questions about contributing, open an issue with the `question` label.
