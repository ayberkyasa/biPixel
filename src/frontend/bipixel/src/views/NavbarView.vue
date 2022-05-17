<template>
  <div>
    <v-navigation-drawer app>
      <v-list>
        <v-list-item two-line style="justify-content: center">
          <v-icon x-large style="font-size: 150px">mdi-account</v-icon>
        </v-list-item>

        <v-list-item>
          <v-list-item-content>
            <v-list-item-title class="text-center" style="font-size: 28px">
              {{ fullName }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>

        <div v-if="this.$store.state.userType === 'Customer'">
          <router-link
            v-for="item in customerListItems"
            :key="item.title"
            tag="v-list-item"
            :to="item.path"
            link
          >
            <v-list-item-icon>
              <v-icon>{{ item.icon }}</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>{{ item.title }}</v-list-item-title>
            </v-list-item-content>
          </router-link>
        </div>

        <div v-else>
          <router-link
            v-for="item in employeeListItems"
            :key="item.title"
            tag="v-list-item"
            :to="item.path"
            link
          >
            <v-list-item-icon>
              <v-icon>{{ item.icon }}</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>{{ item.title }}</v-list-item-title>
            </v-list-item-content>
          </router-link>
        </div>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar flat app>
      <v-spacer></v-spacer>
      <v-app-bar-title>
        <span class="font-weight-light display-2">bi</span
        ><span class="display-2">Pixel</span>
      </v-app-bar-title>
      <v-spacer></v-spacer>
      <v-btn text color="red" @click="logout">
        LOGOUT
        <v-icon class="ml-1">mdi-exit-to-app</v-icon>
      </v-btn>
    </v-app-bar>
  </div>
</template>

<script>
import axiosInstance, { URL } from "../services/axiosConfig.js";
export default {
  data() {
    return {
      fullName: "",
      customerListItems: [
        { title: "Profile", icon: "mdi-account", path: "/profile" },
        { title: "Social", icon: "mdi-account-multiple", path: "/social" },
        { title: "Rent Movie", icon: "mdi-movie-search", path: "/rent" },
        {
          title: "Withdraw Movie",
          icon: "mdi-movie-remove",
          path: "/withdraw",
        },
        { title: "Favorite List", icon: "mdi-heart", path: "/favorites" },
        {
          title: "Request Movie",
          icon: "mdi-form-select",
          path: "/request",
        },
      ],
      employeeListItems: [
        { title: "Profile", icon: "mdi-account", path: "/profile" },
        { title: "Social", icon: "mdi-account-multiple", path: "/social" },
        { title: "Rent Movie", icon: "mdi-movie-search", path: "/rent" },
        {
          title: "Withdraw Movie",
          icon: "mdi-movie-remove",
          path: "/withdraw",
        },
        { title: "Favorite List", icon: "mdi-heart", path: "/favorites" },
        {
          title: "Movie Control",
          icon: "mdi-movie-edit",
          path: "/movie-control",
        },
        {
          title: "Customer Control",
          icon: "mdi-account-edit",
          path: "/customer-control",
        },
        {
          title: "Inspect Report",
          icon: "mdi-file-chart",
          path: "/report",
        },
      ],
    };
  },
  methods: {
    logout() {
      this.$store.commit("logOut");
      this.$router.push("/");
    },
    async getFullName() {
      try {
        const response = await axiosInstance.get(URL.GET_USER, {
          params: {
            id: this.$store.state.uid,
          },
        });
        this.fullName = response.data.full_name;
      } catch (exception) {
        this.fullName = "";
      }
    },
  },
  mounted() {
    this.getFullName();
  },
};
</script>
