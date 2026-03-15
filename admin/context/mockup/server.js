const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;
const BASE_DIR = __dirname;

// Serve static assets
app.use('/static', express.static(path.join(BASE_DIR, 'static')));

// ── Helpers ──────────────────────────────────────────────────────────────────

function readFile(relPath) {
  const fullPath = path.join(BASE_DIR, relPath);
  if (!fs.existsSync(fullPath)) return null;
  return fs.readFileSync(fullPath, 'utf8');
}

function assemblePage(role, page) {
  const shell   = readFile('partials/shell.html');
  let header  = readFile('partials/header.html');
  const sidebar = readFile(`partials/sidebar-${role}.html`);
  const footer  = readFile('partials/footer.html');
  const content = readFile(`${role}/content/${page}.html`);

  if (!shell || !header || !sidebar || !footer) {
    return '<h1>Missing partials. Run mockup generator again.</h1>';
  }

  // Inject role into header
  header = header.replace(/\{\{ROLE\}\}/g, role);

  if (!content) {
    return shell
      .replace('{{HEADER}}', header)
      .replace('{{SIDEBAR}}', sidebar)
      .replace('{{CONTENT}}', '<div class="p-6 text-red-500">Page not found: ' + page + '</div>')
      .replace('{{FOOTER}}', footer);
  }
  return shell
    .replace('{{HEADER}}', header)
    .replace('{{SIDEBAR}}', sidebar)
    .replace('{{CONTENT}}', content)
    .replace('{{FOOTER}}', footer);
}

// ── Routes ───────────────────────────────────────────────────────────────────

// Landing page
app.get('/', (req, res) => {
  res.sendFile(path.join(BASE_DIR, 'MOCKUP.html'));
});

// Role home redirect
app.get('/:role', (req, res) => {
  res.redirect(`/${req.params.role}/home`);
});

// Full page (server-assembled from partials + content)
app.get('/:role/:page', (req, res) => {
  const { role, page } = req.params;
  const html = assemblePage(role, page);
  res.send(html);
});

// HTMX content-only endpoint (returns fragment for in-page swaps)
app.get('/api/content/:role/:page', (req, res) => {
  const { role, page } = req.params;
  const content = readFile(`${role}/content/${page}.html`);
  if (!content) return res.status(404).send('<div class="p-6 text-red-500">Content not found.</div>');
  res.send(content);
});

// ── Start ─────────────────────────────────────────────────────────────────────

app.listen(PORT, () => {
  console.log('');
  console.log('  Admin Portal Mockup Server running at:');
  console.log(`  → http://localhost:${PORT}`);
  console.log('');
  console.log('  Open the URL above in your browser to review mockups.');
  console.log('');
});
