/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      textColor: {
        primaryBlue: "#388EEF"
      },
      backgroundColor: {
        primaryBlue: '#388EEF'
      },
      screens: {
        xs: '375px'
      }
    },
  },
  plugins: [],
}

