import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isLoggedIn: false,
    uid: null,
  },
  mutations: {
    logIn(state, id) {
      state.isLoggedIn = true;
      state.uid = id;
    },
    logOut(state) {
      state.isLoggedIn = false;
      state.uid = null;
    },
  },
});
