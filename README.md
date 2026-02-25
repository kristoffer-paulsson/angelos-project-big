# BigInt - Angelos Project™
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/476df052444b4679876a6d6ff3e9a81d)](https://app.codacy.com/gh/angelos-project/angelos-project-big/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

A high-quality arbitrary-precision big integer library implemented in pure Kotlin/Common for effective multiplatform support.

## Features

### Core Arithmetic Operations
- Addition, subtraction, multiplication, and division with remainder
- Square root calculation with remainder support
- Greatest Common Divisor (GCD) computation
- Modular arithmetic (Mod, Pow)
- Random number generation

### Bit Manipulation
- Bitwise operations (AND, OR, XOR, NOT, AND-NOT)
- Bit shifting (left/right)
- Individual bit operations (set, clear, flip, test)
- Bit length and bit count calculations

### Advanced Features
- Multi-platform support through Kotlin Multiplatform
- Efficient big-endian integer array representation
- Comprehensive fuzzing test suite for operation validation
- Serialization/deserialization support
- Unsigned operations handling
- Exception handling through BigMathException

### Performance Optimizations
- Specialized magnitude handling
- Efficient carry propagation in arithmetic operations
- Optimized division algorithm
- Smart memory management

## Quality Assurance

### Fuzzing Coverage
The library includes extensive fuzzing tests for core operations:
- Arithmetic operations (addition, multiplication, division)
- Bit manipulations (shifts, AND, OR, XOR)
- Conversion operations (to/from Long)
- Special operations (abs, max, pow)

### Testing
- Comprehensive unit test suite
- Property-based testing
- Java BigInteger compatibility tests
- Cross-platform validation

## Usage

For use in a Kotlin/Multiplatform project, or compatible, publish the library to your own local maven repository.

1. Run `./gradlew publishToMavenLocal`
2. Add the dependency `org.angproj.big:angelos-project-big:X.Y.Z`
3. Replace `X.Y.X` with version number in `library/build.gradle.kts`

## Tests

Run `./gradlew clean build allTests` for unit tests.


## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository.

## Getting involved

If you want to contribute to the project, please read the projects:

* [Mission statement](https://github.com/angelos-project/.github/blob/master/profile/README.md)
* [Contributor License Agreement (CLA)](https://github.com/angelos-project/.github/blob/master/misc/ADMISSION.md)
* [Code of conduct](https://github.com/angelos-project/.github/blob/master/docs/CODE_OF_CONDUCT.md)
* [Contributing guidelines](https://github.com/angelos-project/.github/blob/master/docs/CONTRIBUTING.md)

We welcome contributions of all kinds, including bug fixes, new features, and documentation improvements. please fork the repository and submit a pull request with your updates.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.