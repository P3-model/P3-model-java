# 1. Libraries for parsing Java code

## Reason

Before we can start to write a P3 model Java parser,\
we need to decide which code parsing libraries to use, initially.

## Status

1. Proposed: **2023-09-25**
2. Accepted/Declined: **2023-10-02**

## Context

### Preliminary findings (research phase)

We found several Java parsing libraries during our research of OSS projects.

#### Direction of scanning (depth vs breadth)

It turns out that there are main **two** **categories** of their direction of scanning:

1. **Single file/class analyser** (**depth** search). It will build an
   [AST tree](https://en.wikipedia.org/wiki/Abstract_syntax_tree)
   with all the details, comments, dependencies and relations for specific single file/class.
2. **Classpath class scanner** (**breadth** search). It will scan the whole classpath,
   find all the classes (might be filtered by annotations used, packages, etc.) and build a list of them,
   but without deep details like AST tree have (only simple and limited ones).

#### What we need in the first place?

The **second** category (**breadth**) is more suitable for our needs, because we need to scan the whole classpath
to find even completely unrelated classes, but with specific annotations used. We cannot just scan a single file/class,
because the AST tree will not contain all information about other classes around it.

All project classes can be grouped to be analysed, tagged and then used for further processing
(e.g. to just make users aware, that they are possibly not documented areas without P3 annotations).

#### What we can use additionally?

The **first** category (**depth**) can be used additionally to the second one as a complementary tool.

Let's say we have to retrieve detailed information about public contract of a specific class (even with comments around
it)
then we can use the **depth** search to find it in the AST tree of the class (using some Visitor pattern strategies).

**For the beginning we will not use it** probably, but within this ADR we will list a several library candidates for the
future decisions.

#### Conclusion (TL;DR)

We will use the **second** category (**breadth**) lib for the initial phase of the project.

In addition, we will list the possible libs from the **first** category (**depth**) for the complementary purposes.

### Libraries comparison

We have compared the following libraries divided into two categories under the specific criteria:

- license compatibility
- contribution activity
- support for JVM languages
- needed functionality coverage
- performance
- open issues

#### Category: classpath scanner (breadth)

Table with concrete library in columns and the criteria in rows:

| Criteria / Library name       | Classgraph                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | Reflections                                                                                                                                                                  
|-------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Repository URL                | https://github.com/classgraph/classgraph                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | https://github.com/ronmamo/reflections                                                                                                                                       |
| License compatibility         | &#10004; [MIT](https://github.com/classgraph/classgraph/blob/latest/LICENSE-ClassGraph.txt) - Using MIT licensed lib in a CC BY-SA 4.0 project is legally okay. To respect the terms of the MIT license, it is needed to attach include the copyright notice and permission notice.<br/><br/> For instance we can create a sepearate file `NOTICES.md`, which will contain list of copyright-related notices from all project dependencies. This ensures transparency for anyone interested in P3 model Java project licensing details. | &#10004; [WTFPL](https://github.com/ronmamo/reflections/blob/master/COPYING.txt) - No restrictions at all, everything is allowed.                                            |
| Contribution activity         | &#10004; Active development <br/> **Last released:** August 2023.                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | &#10006; Currently **NOT** under active development or maintenance.<br/> **Last released:** October 2021.                                                                    |
| Support for JVM languages     | <ul><li>&#10004; Java: 7, 8, 9+ (for 16+ please be aware of [that](https://github.com/classgraph/classgraph/wiki#running-on-jdk-16))</li><li>&#10004; Kotlin</li><li>&#10004; Scala</li></ul>                                                                                                                                                                                                                                                                                                                                           | <ul><li>&#10004; Java: 8+</li><li>&#10004; Kotlin (some [issues](https://github.com/ronmamo/reflections/issues/270) occurs)</li><li>&#63; Scala: No info available</li></ul> |
| Needed functionality coverage | &#10004; Enough coverage: classpath scanner, module scanner, even code duplication detection                                                                                                                                                                                                                                                                                                                                                                                                                                            | &#10006; Partial coverage: classpath scanner, but without modules (introduced in Java 9), basic API (which might be a plus for the beginners)                                |
| Performance                   | &#10004; ClassGraph scans the classpath or module path using carefully optimized multithreaded code for the shortest possible scan times, and it runs as close as possible to I/O bandwidth limits, even on a fast SSD.                                                                                                                                                                                                                                                                                                                 | &#63; Info N/A                                                                                                                                                               |
| Open issues                   | 14 (latest status: ![GitHub issues](https://img.shields.io/github/issues/classgraph/classgraph.svg))                                                                                                                                                                                                                                                                                                                                                                                                                                    | 114 (latest status: ![GitHub issues](https://img.shields.io/github/issues/ronmamo/reflections.svg))                                                                          |

### Supplement: Single file/class analysers (depth)

The possible library candidates from for the complementary purposes (in the future decisions) are:

- JavaParser: https://javaparser.org/
- Roaster: https://github.com/forge/roaster
- Spoon: https://github.com/INRIA/spoon

## Decision

We will use the **[Classgraph](https://github.com/classgraph/classgraph)** library for the initial phase of the project.

It will fit our needs the best, it is actively developed, has enough functionality coverage and even care about
performance.

There is low risk of being abandoned in the future. It is also compatible with our license.

## Consequences

As already mentioned in the **Libraries comparison** section, we will have to create a separate file `NOTICES.md`,
which will contain list of all project dependencies with their license-related notices (including Classgraph).

Thanks to the good documentation, community activity and support for JVM languages, we will be able to use it without
any bigger issues.
