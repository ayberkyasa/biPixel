import Vue from "vue";
import VueRouter from "vue-router";
import LoginRegisterView from "../views/LoginRegisterView.vue";
import ProfileView from "../views/ProfileView.vue";
import RentView from "../views/RentView.vue";
import WithdrawView from "../views/WithdrawView.vue";
import SocialNetworkView from "../views/SocialNetworkView.vue";
import FavoriteListView from "../views/FavoriteListView.vue";
import MovieControlView from "../views/MovieControlView.vue";
import CustomerControlView from "../views/CustomerControlView.vue";
import MovieRequestView from "../views/MovieRequestView.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    component: LoginRegisterView,
  },
  {
    path: "/profile",
    component: ProfileView,
  },
  {
    path: "/rent",
    component: RentView,
  },
  {
    path: "/withdraw",
    component: WithdrawView,
  },
  {
    path: "/social",
    component: SocialNetworkView,
  },
  {
    path: "/favorites",
    component: FavoriteListView,
  },
  {
    path: "/movie-control",
    component: MovieControlView,
  },
  {
    path: "/customer-control",
    component: CustomerControlView,
  },
  {
    path: "/request",
    component: MovieRequestView,
  },
];

const router = new VueRouter({
  routes,
  mode: "history",
});

export default router;
