import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  build: {
    outDir: '../resources/static',
    emptyOutDir: false,
    manifest: true,
    rollupOptions: {
      input: {
        app: resolve(__dirname, 'src/main.js'),
        styles: resolve(__dirname, 'src/main.css'),
      },
      output: {
        entryFileNames: 'js/[name]-[hash].js',
        chunkFileNames: 'js/[name]-[hash].js',
        assetFileNames: (assetInfo) => {
          if (assetInfo.name && assetInfo.name.endsWith('.css')) {
            return 'css/[name]-[hash][extname]';
          }
          return 'assets/[name]-[hash][extname]';
        },
      },
    },
  },
  server: {
    origin: 'http://localhost:5173',
  },
});
