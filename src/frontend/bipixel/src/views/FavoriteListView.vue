<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Favorite List</v-row>
    <v-data-table
      :headers="headers"
      :items="favorites"
      item-key="email"
      class="elevation-1"
    >
      <template v-slot:[`item.details`]="{ item }">
        <v-btn
          color="green lighten-1"
          rounded
          small
          dark
          @click="showDetails(item)"
          @click.stop="detailsDialog = true"
          >Details</v-btn
        >
      </template>
      <template v-slot:[`item.remFav`]="{ item }">
        <v-btn
          class="mx-2"
          fab
          dark
          x-small
          color="pink"
          @click="remFavourites(item)"
        >
          <v-icon> mdi-close </v-icon>
        </v-btn>
      </template>
    </v-data-table>
    <v-divider></v-divider>
    <v-dialog v-model="detailsDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Movie Details </v-card-title>

        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Duration: {{ showedMovie.duration }} (min)</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Language Option: {{ showedMovie.language_option }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Subtitle Option: {{ showedMovie.subtitle_option }}</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Actors: {{ actors }}</strong>
          </v-row>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="detailsDialog = false">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      actors: "",
      showedMovie: {},
      detailsDialog: false,
      favorites: [],
    };
  },
  computed: {
    headers() {
      return [
        {
          text: "Title",
          align: "start",
          value: "title",
          sortable: false,
        },
        {
          text: "Director",
          align: "start",
          value: "directors",
          filterable: false,
          sortable: false,
        },
        {
          text: "Genre",
          sortable: false,
          align: "start",
          value: "genres",
        },
        {
          text: "Rate",
          align: "start",
          value: "overall_rating",
          sortable: false,
        },
        {
          text: "Year",
          align: "start",
          filterable: false,
          value: "production_year",
          sortable: false,
        },
        {
          text: "Price (TL)",
          align: "start",
          value: "price",
          sortable: false,
        },
        {
          text: "Details",
          align: "center",
          value: "details",
          filterable: false,
          sortable: false,
        },
        {
          text: "Remove Favourites",
          align: "center",
          value: "remFav",
          filterable: false,
          sortable: false,
        },
      ];
    },
  },
  methods: {
    async getFavorites() {
      try {
        const res = await axiosInstance.get(URL.GET_FAVORITE_LIST, {
          params: {
            userId: this.$store.state.uid,
          },
        });
        this.favorites = res.data;
      } catch (error) {
        console.log("err");
      }
    },
    showDetails(value) {
      console.log(value);
      this.showedMovie = value;
      this.showActors();
    },
    showActors() {
      this.showedMovie.actors.forEach((item) => {
        this.actors += item + ", ";
      });
    },
    select(value) {
      this.selected = value.value;
    },
    async remFavourites(value) {
      try {
        const res = await axiosInstance.delete(URL.DELETE_FAVORITE, {
          data: {
            userId: this.$store.state.uid,
            movieId: value.movie_id,
          },
        });
        console.log(res);
        await this.getFavorites();
      } catch (error) {
        console.log("er");
      }
    },
  },
  async created() {
    await this.getFavorites();
  },
};
</script>
