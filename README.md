# BigInt - Angelos Project™
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/476df052444b4679876a6d6ff3e9a81d)](https://app.codacy.com/gh/angelos-project/angelos-project-big/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

A high-quality arbitrary-precision big integer library implemented in pure Kotlin/Common for effective multiplatform support.

It covers all necessary features for descent cryptographic use such as implementing ECDSA (not included).

## Usage

For use in a Kotlin/Multiplatform project, or compatible, publish the library to your own local maven repository.

1. Run `./gradlew publishToMavenLocal`
2. Add the dependency `org.angproj.big:angelos-project-big:X.Y.Z`
3. Replace `X.Y.X` with version number in `library/build.gradle.kts`

## Tests

Run `./gradlew clean build allTests` for unit tests.

Fuzzing covers all math operations and is designed to run for up to 2 minutes without crashing in compatibility mode 
against Java BigInteger. The fuzzer generates random inputs and validating the results against expected outcome, the 
same Java, thereby fully compatible. To run the fuzzer use IntelliJ IDEA and click the green arrow fron withing the
Fuzzer*.kt files found in the `:jazzer` module.

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