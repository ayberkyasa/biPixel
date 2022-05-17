/* eslint-disable */
import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store";
import LoginRegisterView from "../views/LoginRegisterView.vue";
import ProfileView from "../views/ProfileView.vue";
import RentView from "../views/RentView.vue";
import WithdrawView from "../views/WithdrawView.vue";
import SocialNetworkView from "../views/SocialNetworkView.vue";
import FavoriteListView from "../views/FavoriteListView.vue";
import MovieControlView from "../views/MovieControlView.vue";
import CustomerControlView from "../views/CustomerControlView.vue";
import MovieRequestView from "../views/MovieRequestView.vue";
import ReportView from "../views/ReportView";

Vue.use(VueRouter);

const isAuthenticated = function(to, from, next) {
  if (store.state.isLoggedIn) {
    next();
  } else {
    next("/");
  }
};

const routes = [
  {
    path: "/",
    component: LoginRegisterView,
  },
  {
    path: "/profile",
    component: ProfileView,
    beforeEnter: isAuthenticated,
  },
  {
    path: "/rent",
    component: RentView,
    beforeEnter: isAuthenticated,
  },
  {
    path: "/withdraw",
    component: WithdrawView,
    beforeEnter: isAuthenticated,
  },
  {
    path: "/social",
    component: SocialNetworkView,
    beforeEnter: isAuthenticated,
  },
  {
    path: "/favorites",
    component: FavoriteListView,
    beforeEnter: isAuthenticated,
  },
  {
    path: "/movie-control",
    component: MovieControlView,
    beforeEnter: isAuthenticated,
  },
  {
    path: "/customer-control",
    component: CustomerControlView,
    beforeEnter: isAuthenticated,
  },
  {
    path: "/request",
    component: MovieRequestView,
    beforeEnter: isAuthenticated,
  },
  {
    path: "/report",
    component: ReportView,
    beforeEnter: isAuthenticated,
  },
];

const router = new VueRouter({
  routes,
  mode: "history",
});

export default router;
