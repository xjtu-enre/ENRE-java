#! /usr/bin/env python3

import subprocess
import os

ENRE_REPO = os.getcwd()
TEST_REPO = "enre-java-test"

subprocess.run(["git", "submodule", "init"])
subprocess.run(["git", "submodule", "update"])
os.chdir(TEST_REPO)
subprocess.run(["git", "pull", "origin", "main"])
subprocess.run(["git", "checkout", "main"])
os.chdir(ENRE_REPO)
