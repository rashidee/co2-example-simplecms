const express = require('express');
const path = require('path');
const fs = require('fs');

const app = express();
const PORT = 3000;

// Static file serving
app.use('/static', express.static(path.join(__dirname, 'static')));

// GET / — Landing page (home)
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'pages', 'home.html'));
});

// GET /blog — Blog directory
app.get('/blog', (req, res) => {
  res.sendFile(path.join(__dirname, 'pages', 'blog.html'));
});

// GET /blog/:slug — Blog detail
app.get('/blog/:slug', (req, res) => {
  res.sendFile(path.join(__dirname, 'pages', 'blog_detail.html'));
});

app.listen(PORT, () => {
  console.log(`Simple CMS Landing Page mockup server running at http://localhost:${PORT}`);
  console.log(`Sitemap / index: open MOCKUP.html in your browser`);
});
