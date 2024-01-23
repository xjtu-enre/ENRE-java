#! /usr/bin/env python3

import sys
import os
import shutil
import subprocess

ENRE_REPO = os.getcwd()
TEST_REPO = os.path.join(ENRE_REPO, "enre-java-test")

enre_paths = {
    "docs": os.path.join(ENRE_REPO, "docs"),
    "suites": os.path.join(ENRE_REPO, "src", "test", "java", "client"),
    "cases": os.path.join(ENRE_REPO, "src", "test", "resources", "cases"),
}

test_paths = {
    "docs": os.path.join(TEST_REPO, "docs"),
    "tests": os.path.join(TEST_REPO, "tests"),
    "suites": os.path.join(TEST_REPO, "tests", "suites"),
    "cases": os.path.join(TEST_REPO, "tests", "cases"),
}

def preprocess():
  if os.path.isdir(test_paths["docs"]):
    shutil.rmtree(test_paths["docs"])
  if os.path.isdir(test_paths["tests"]):
    shutil.rmtree(test_paths["tests"])
  if os.path.isdir(enre_paths["suites"]):
    shutil.rmtree(enre_paths["suites"])
  if os.path.isdir(enre_paths["cases"]):
    shutil.rmtree(enre_paths["cases"])
  shutil.copytree(enre_paths["docs"], test_paths["docs"])
  os.makedirs(enre_paths["suites"], exist_ok=True)
  # os.makedirs(enre_paths["cases"], exist_ok=True)
  return

def build():
  os.chdir(TEST_REPO)
  subprocess.run(["npm", "install"])
  subprocess.run(["node", "--experimental-specifier-resolution", "node", os.path.join("src", "index.js")])
  os.chdir(ENRE_REPO)
  for file in os.listdir(test_paths["suites"]):
    if not file.endswith(".java"):
      continue
    shutil.copy(os.path.join(test_paths["suites"], file), enre_paths["suites"])
  shutil.copytree(test_paths["cases"], enre_paths["cases"])

if __name__ == '__main__':
  preprocess()
  build()
