/** @type {import('tailwindcss').Config} */
module.exports = {
  purge: [
    "./src/components\\pages\\User\\UserLoginView.vue",
    "./src/components/component/NavBar.vue",
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('@tailwindcss/forms'),
  ],
 
}
