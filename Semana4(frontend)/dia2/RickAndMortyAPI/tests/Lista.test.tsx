import assert from "node:assert/strict";
import test from "node:test";
import { renderToStaticMarkup } from "react-dom/server";
import { Lista } from "../src/Lista.js";

test("muestra los cinco elementos recibidos mediante props", () => {
  const elementos = ["HTML", "CSS", "JavaScript", "TypeScript", "React"];

  const html = renderToStaticMarkup(<Lista elementos={elementos} />);

  for (const elemento of elementos) {
    assert.match(html, new RegExp(`<li>${elemento}</li>`));
  }

  assert.equal((html.match(/<li>/g) ?? []).length, 5);
});
