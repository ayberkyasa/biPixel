import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isLoggedIn: false || (localStorage.getItem("isLoggedIn") === "true"),
    uid: null,
    userType: "",
  },
  mutations: {
    logIn(state, payload) {
      localStorage.setItem("isLoggedIn", true);
      state.isLoggedIn = true;
      state.uid = payload.id;
      state.userType = payload.userType;
    },
    logOut(state) {
      localStorage.removeItem("isLoggedIn");
      state.isLoggedIn = false;
      state.uid = null;
      state.userType = "";
    },
  },
});
