# Production JAR server smoke test

From the repository root, run:

```sh
python3 qa/server_smoke.py --jar versions/26.3-fabric/build/libs/simple_smithing_overhaul-fabric-2.9.14-port.1+26.3.jar --mods build/qa-dependencies
```

Use the actual production JAR filename. The dependency directory must contain only
the required runtime dependency JARs for 26.3, including their required transitive
dependencies. Fabric Loader enforces the dependencies declared by these artifacts.
The harness never reads an existing profile's mods or worlds. Optional test-only
fixture JARs can be added with repeated `--fixture /path/to/fixture.jar` arguments.

Prerequisites: Python 3, Java 25 (`--java` overrides discovery), cached official
Minecraft 26.3 server bundle and Fabric Loader 0.19.5 with its libraries in the
Gradle cache (`--cache` overrides its location). The harness does not download
anything. Running the harness writes `eula=true` for its disposable test server;
run it only if you accept the Minecraft EULA.

Each invocation creates a fresh directory below `build/qa-run/`. It checks Fabric
library SHA-1 values against the loader manifest and Minecraft embedded SHA-256
values against its bundle manifest, verifies copied files by SHA-256, and records
all JAR hashes and source paths in `launch-audit.json`. It starts a localhost-only
server, waits for startup, requests a datapack reload, waits for
advancements to load, and stops the server. Nonzero exit, timeout, or error logs
fail the check. `console.log` and `result.json` preserve the evidence.

This verifies dedicated-server startup and reload of the production artifact; it
does not exercise client rendering or player interaction. `qa/` is outside main
source/resources and must not be included in release JARs.

The optional repair fixture checks the registered repairable components for the
nine vanilla items added by this mod, accepts/rejects repair materials, and invokes
the actual portable repair recipe with a damaged bow, string, and flint. It checks
the repair amount, repair counter, original input, consumed materials, and rejection
of an incorrect material, both at startup and after reload:

```sh
python3 qa/build_fixture.py --jar versions/26.3-fabric/build/libs/simple_smithing_overhaul-fabric-2.9.14-port.1+26.3.jar --mods build/qa-dependencies
python3 qa/server_smoke.py --jar versions/26.3-fabric/build/libs/simple_smithing_overhaul-fabric-2.9.14-port.1+26.3.jar --mods build/qa-dependencies --fixture build/qa-fixture/sso-qa-repair-checks.jar
```

Repeat the second command with `--repair-overrides --config-template
build/qa-run/<default-run>/config/simple_smithing_overhaul/config-v2.toml` to seed the disposable config
with a fishing rod -> stick override and a pickaxes tag -> planks tag override.
The fixture checks those replacements (including rejection of the old materials)
alongside the other defaults. Fixture compilation uses the production mod,
dependency JARs including nested libraries, and the cached official server.
