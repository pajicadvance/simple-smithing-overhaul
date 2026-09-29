# Fabric 26.3 verification

Verified 2026-09-29 from upstream `1d1448b2d5f7f1d68b3e6e5cc1d5a869df65061a` plus this patch.

Release: `simple_smithing_overhaul-fabric-2.9.14-port.1+26.3.jar`

SHA-256: `fde7de61945f008e716f71711f8eeed27d4b123e069893f381ee0bda2490587f`

- `bash gradlew :26.3-fabric:build :26.2-fabric:compileJava --configure-on-demand --no-configuration-cache --max-workers=2` passed using Microsoft JDK 25.0.4.1 and the upstream Gradle 9.7.1 wrapper.
- Minecraft 26.3 loot-codec roundtrips preserve template probabilities (including zero and one), enchanted-book modifiers, stack counts, and broken-anvil explosion conditions. Preparing pools neither mutates shared templates nor changes already-prepared pools.
- Component tests cover reload item/material tag membership, explicit item overrides, unchanged unrelated components, empty mappings, absent optional targets, and invalid mapping handling.
- Production-JAR dedicated-server checks passed startup, datapack reload and clean shutdown with default repair mappings and with configured item/tag overrides. Tests verify nine vanilla repairable items, valid/invalid repair materials, portable bow repair amount and count, unchanged input, and consumed repair materials.
- The override profile verifies fishing rods repaired with sticks and iron/diamond pickaxes repaired with planks, including rejection of their previous materials. Checks repeat after reload.
- A final fixture-free production-JAR server also passed startup, reload and clean shutdown (`build/qa-run/20260929-105021-50821/`).
- ZIP integrity, Fabric metadata, entrypoints, absence of test fixtures/classes, and SHA-256 agreement between the tested and released JAR passed.

Runtime dependencies: Fabric Loader 0.19.5, Fabric API 0.161.0+26.3, Mixson 2.2.1, Fzzy Config 0.7.7+fix2+26.3, Fabric Language Kotlin 1.14.1+kotlin.2.4.20. Neither Defaulted nor CodecUI was installed.

Local ignored evidence: `build/qa-run/20260929-104807-45862/` (defaults), `build/qa-run/20260929-104911-48655/` (overrides), with launch audits, full logs and result JSON. Reproduction commands are in [qa/README.md](qa/README.md).

Graphical client behavior, real player network interactions, optional-mod integrations, and NeoForge were not tested. The 26.2 check verifies compilation only.
