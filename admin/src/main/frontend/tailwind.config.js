/** @type {import('tailwindcss').Config} */
export default {
  content: [
    '../jte/**/*.jte',
    './src/**/*.js',
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: '#2271b1',
        secondary: '#135e96',
        accent: '#2271b1',
        success: '#00a32a',
        warning: '#dba617',
        danger: '#d63638',
        sidebar: '#1d2327',
        'sidebar-text': '#f0f0f1',
        'page-bg': '#f0f0f1',
        surface: '#ffffff',
        'text-primary': '#1e1e1e',
        'text-secondary': '#646970',
        border: '#c3c4c7',
      },
      fontFamily: {
        body: [
          '-apple-system',
          'BlinkMacSystemFont',
          '"Segoe UI"',
          'Roboto',
          'Oxygen-Sans',
          'Ubuntu',
          'Cantarell',
          '"Helvetica Neue"',
          'sans-serif',
        ],
      },
      borderRadius: {
        btn: '4px',
        card: '8px',
      },
    },
  },
  plugins: [],
};
