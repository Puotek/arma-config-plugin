# Arma 3 Config IntelliJ Plugin

![Build](https://github.com/Puotek/arma-config-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/29234-arma-config-support.svg)](https://plugins.jetbrains.com/plugin/29234-arma-config-support)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/29234-arma-config-support.svg)](https://plugins.jetbrains.com/plugin/29234-arma-config-support)

<!-- Plugin description -->
Language support for Arma 3-style configuration files (`config.cpp`, `.hpp`, etc.) in IntelliJ-based IDEs.

## Features
- Syntax highlighting for classes, assignments, macros, numeric expressions, and comments.</li>
- Breadcrumbs and sticky lines for navigating config class hierarchies.</li>
- Code folding for classes and array blocks.</li>
- Inspections for:
- Comma inside parentheses (weak warning). (Possible macro conflict)</li>
- Array parameter bracket / brace mismatch (warning).</li>
- Custom color settings page to fine-tune highlighting for Arma Config code.</li>
- Support for macro invocations in class names and values.</li>
<br>
Designed for myself, since I like working in IntelliJ instead of VS Code, and the current existing arma plugin was old and didnt support some of the nicer newer IntelliJ features like breadcrumbs and sticky lines.

<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "arma-config-plugin"</kbd> >
  <kbd>Install</kbd>

- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/29234-arma-config-support) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/29234-arma-config-support/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/Puotek/arma-config-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manual Build:

  Open project in IntelliJ

  You will need plugins: 
  - [Plugin DevKit](https://plugins.jetbrains.com/plugin/22851-plugin-devkit)
  - [Grammar-Kit](https://plugins.jetbrains.com/plugin/6606-grammar-kit)
  
  Make sure to set an SDK to `openjdk-21 Oracle OpenSDK 21.0.1` in `File` > `Project Structure`

  Delete `src/main/gen/arma` folder and rebuild it by `RMB` on `src/main/grammar/ArmaConfig.bnf` and select `Generate Parser Code`

  In right panel open gradle and run task `arma-config-plugin/Tasks/puotek/buildDownloads`

  You should get a plugin build in your User/Downloads Folder

  Install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
