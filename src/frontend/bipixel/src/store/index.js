import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isLoggedIn: false,
    uid: null,
    userType: "",
  },
  mutations: {
    logIn(state, payload) {
      state.isLoggedIn = true;
      state.uid = payload.id;
      state.userType = payload.userType;
    },
    logOut(state) {
      state.isLoggedIn = false;
      state.uid = null;
      state.userType = "";
    },
  },
});
