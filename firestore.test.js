const { test } = require("node:test");
const fs = require("node:fs");
const assert = require("node:assert");

test("Firestore security rules verification", () => {
  const rules = fs.readFileSync("firestore.rules", "utf8");
  assert.ok(rules.includes("rules_version = '2';"), "Rules version 2 required");
  assert.ok(rules.includes("service cloud.firestore"), "Service cloud.firestore required");
  assert.ok(rules.includes("isOwner(userId)"), "isOwner helper function required");
  assert.ok(rules.includes("isValidUser"), "User validation function required");
  assert.ok(rules.includes("match /users/{userId}"), "User collection path required");
});
