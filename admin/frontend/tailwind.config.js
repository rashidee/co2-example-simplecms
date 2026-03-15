/** @type {import('tailwindcss').Config} */
export default {
  content: [
    '../src/main/jte/**/*.jte',
    './src/js/**/*.js',
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#2271b1',
          hover: '#135e96',
          light: '#e8f0fe',
        },
        sidebar: {
          DEFAULT: '#1d2327',
          text: '#c3c4c7',
        },
        success: '#00a32a',
        warning: '#dba617',
        danger: '#d63638',
      },
      fontFamily: {
        body: ['-apple-system', 'BlinkMacSystemFont', '"Segoe UI"', 'Roboto', 'Oxygen-Sans', 'Ubuntu', 'Cantarell', '"Helvetica Neue"', 'sans-serif'],
      },
    },
  },
  plugins: [],
};
