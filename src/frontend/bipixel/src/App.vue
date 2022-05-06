<template>
  <v-app>
    <nav-bar v-if="true"></nav-bar>
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
import Navbar from "./views/NavbarView.vue";
export default {
  name: "App",
  components: {
    navBar: Navbar,
  },
  data: () => ({
    //
  }),
  computed: {
    isLoggedIn() {
      return this.$store.state.isLoggedIn;
    },
  },
};
</script>
