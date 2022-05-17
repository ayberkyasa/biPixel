<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Reports</v-row>
    <v-tabs color="deep-blue accent-4" v-model="tab">
      <v-tabs-slider color="blue"></v-tabs-slider>
      <v-tab>Report 1</v-tab>
      <v-tab>Report 2</v-tab>
      <v-tab>Report 3</v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <div class="d-flex justify-end">
        <v-tooltip bottom>
          <template v-slot:activator="{ on, attrs }">
            <v-icon
              class=""
              large
              color="primary"
              dark
              v-bind="attrs"
              v-on="on"
            >
              mdi-information
            </v-icon>
          </template>
          <p v-if="tab === 0">
            This report shows how many movies users have rented and the total
            amount of money they spent on this app.
          </p>
          <p class="yellow--text text--lighten-1" v-if="tab === 0">
            Employees rent a movie free. However, the value shown here is the
            sum of the prices of the rented movies in the database.
          </p>

          <span v-if="tab === 1"
            >This report lists how many times each movie in the system has been
            recommended among users.</span
          >
          <span v-if="tab === 2"
            >This report shows the genre names, ratings and the movie titles
            with the most rated movie in that specific genre.</span
          >
        </v-tooltip>
      </div>

      <v-tab-item>
        <v-data-table
          :headers="headers"
          :items="report1"
          item-key="email"
          class="elevation-1"
        >
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers2"
          :items="report2"
          item-key="email"
          class="elevation-1"
        >
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <v-data-table
          :headers="headers3"
          :items="report3"
          item-key="email"
          class="elevation-1"
        ></v-data-table>
      </v-tab-item>
    </v-tabs-items>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      report1: [],
      report2: [],
      report3: [],
      tab: null,
    };
  },
  computed: {
    headers() {
      return [
        {
          text: "Users",
          align: "start",
          value: "full_name",
          sortable: false,
        },
        {
          text: "Number of Rents",
          align: "center",
          value: "num_rent",
          sortable: false,
        },
        {
          text: "Money Spent",
          sortable: false,
          align: "center",
          value: "money_spent",
        },
        {
          text: "",
          sortable: false,
          align: "center",
          value: "info",
        },
      ];
    },
    headers2() {
      return [
        {
          text: "Movies",
          align: "start",
          value: "title",
          sortable: false,
        },
        {
          text: "Number of Recommendations",
          align: "center",
          value: "num_recommend",
          sortable: false,
        },
      ];
    },
    headers3() {
      return [
        {
          text: "Genres",
          align: "start",
          value: "genre_name",
          sortable: false,
        },
        {
          text: "Movies",
          align: "start",
          value: "title",
          filterable: false,
          sortable: false,
        },
        {
          text: "Ratings",
          sortable: false,
          align: "center",
          value: "max_rating",
        },
      ];
    },
  },
  methods: {
    async initialize() {
      try {
        const rep1 = await axiosInstance.get(URL.GET_REPORT_ONE);
        this.report1 = rep1.data;

        const rep2 = await axiosInstance.get(URL.GET_REPORT_TWO);
        this.report2 = rep2.data;

        const rep3 = await axiosInstance.get(URL.GET_REPORT_THREE);
        this.report3 = rep3.data;
      } catch (error) {
        console.log(error);
      }
    },
  },
  async created() {
    await this.initialize();
  },
};
</script>
