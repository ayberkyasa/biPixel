<template>
  <v-container class="pa-16">
    <v-row class="display-1 font-weight-medium mb-6">Delete Movie</v-row>
    <v-row
      ><v-text-field
        v-model="search"
        label="Search by name"
        filled
        rounded
        dense
      ></v-text-field
    ></v-row>
    <v-data-table
      :headers="headers"
      :items="movies"
      item-key="movie_id"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.details`]="{ item }">
        <v-btn
          color="green lighten-1"
          icon
          large
          @click="openDetailsDialog(item)"
          ><v-icon>mdi-information</v-icon></v-btn
        >
      </template>
      <template v-slot:[`item.delete`]="{ item }">
        <v-btn
          color="red darken-1"
          rounded
          small
          dark
          @click="deleteMovie(item)"
          ><v-icon dark>mdi-delete-forever</v-icon></v-btn
        >
      </template>
    </v-data-table>
    <v-divider class="mt-10 blue-grey darken-2"></v-divider>
    <v-row class="display-1 font-weight-medium my-6">Add Movie</v-row>
    <v-row>
      <p
        class="my-4 px-2 py-2 grey lighten-2 red--text text--lighten-2 rounded-pill"
      >
        <v-icon large class="red--text text--lighten-2 mr-2"
          >mdi-alert-circle-outline</v-icon
        ><span
          >Directors, Actors, Genres, Languages, Subtitles fields are
          multivalued so you can enter multi values separated with commas in
          these fields.</span
        >
      </p></v-row
    >
    <v-row>
      <v-col cols="12" md="4" class="pr-16">
        <v-text-field
          v-model="add.title"
          label="Title"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.directors_full_name"
          label="Directors"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.actors_full_name"
          label="Actors"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.production_year"
          label="Year"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.duration"
          label="Duration"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.genres"
          label="Genres"
          outlined
          dense
          rounded
        ></v-text-field>
      </v-col>
      <v-col cols="12" md="4" class="pr-16">
        <v-text-field
          v-model="add.language_option"
          label="Languages"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.subtitle_option"
          label="Subtitles"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.price"
          label="Price"
          outlined
          dense
          rounded
          class="mb-4"
        ></v-text-field>
        <v-row class="text-center"
          ><v-col cols="12" class="text-center"
            ><v-btn color="green darken-1" rounded dark @click="addMovie"
              >SUBMIT</v-btn
            ></v-col
          ></v-row
        >
      </v-col>
    </v-row>
    <v-divider class="mt-10 blue-grey darken-2"></v-divider>
    <v-row class="display-1 font-weight-medium my-6">Manage Requests</v-row>
    <v-data-table
      :headers="headers2"
      :items="requests"
      item-key="req_movie_id"
      class="elevation-1"
    >
      <template v-slot:[`item.accept`]="{ item }">
        <v-btn
          color="green darken-1"
          rounded
          small
          dark
          @click="acceptedMovie = item"
          @click.stop="acceptDialog = true"
          ><v-icon dark>mdi-checkbox-marked-circle</v-icon></v-btn
        >
      </template>
      <template v-slot:[`item.reject`]="{ item }">
        <v-btn
          color="red darken-1"
          rounded
          small
          dark
          @click="rejectRequest(item)"
          ><v-icon dark>mdi-close-circle</v-icon></v-btn
        >
      </template>
    </v-data-table>
    <v-dialog v-model="detailsDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Movie Details </v-card-title>

        <v-card-text class="pl-9">
          <v-row class="text-subtitle-1">
            <strong>Duration: {{ showedMovie.duration }} (min)</strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Language Option: {{ showedMovie.language_option }} </strong>
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
    <v-dialog v-model="acceptDialog" max-width="500">
      <v-card>
        <v-card-title class="text-h5 mb-3"> Specify Details </v-card-title>

        <v-card-text class="pl-9">
          <v-text-field
            v-model="acceptConfig.language_option"
            label="Language Option"
            outlined
            dense
            rounded
            class="mb-4"
          ></v-text-field>
          <v-text-field
            v-model="acceptConfig.subtitle_option"
            label="Subtitle Option"
            outlined
            dense
            rounded
            class="mb-4"
          ></v-text-field>
          <v-text-field
            v-model="acceptConfig.price"
            label="Price"
            outlined
            dense
            rounded
            class="mb-4"
          ></v-text-field>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="acceptRequest">
            Accept
          </v-btn>
          <v-btn color="red darken-1" text @click="closeAcceptDialog">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-snackbar :color="color" timeout="2000" v-model="snackbar">
      {{ text }}
    </v-snackbar>
  </v-container>
</template>
<script>
import axiosInstance, { URL } from "../services/axiosConfig";
export default {
  data() {
    return {
      requests: [],
      add: {
        title: "",
        directors_full_name: "",
        actors_full_name: "",
        production_year: "",
        duration: "",
        genres: "",
        price: "",
        language_option: "",
        subtitle_option: "",
      },
      acceptedMovie: {},
      acceptConfig: {},
      acceptDialog: false,
      showedMovie: {},
      detailsDialog: false,
      search: "",
      price: "",
      rate: "",
      selected: "None",
      movies: [],
      actors: "",
      color: "",
      snackbar: false,
      text: "",
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
          value: "directors_full_name",
          filterable: false,
          sortable: false,
        },
        {
          text: "Genre",
          sortable: false,
          align: "start",
          value: "genres",
          filterable: false,
        },
        {
          text: "Rate",
          align: "start",
          value: "overall_rating",
          sortable: false,
          filterable: false,
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
          filterable: false,
        },
        {
          text: "Details",
          align: "center",
          value: "details",
          filterable: false,
          sortable: false,
        },
        {
          text: "Delete",
          align: "center",
          value: "delete",
          filterable: false,
          sortable: false,
        },
      ];
    },
    headers2() {
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
          value: "directors_full_name",
          sortable: false,
        },
        {
          text: "Genre",
          sortable: false,
          align: "start",
          value: "genres",
        },
        {
          text: "Year",
          align: "start",
          value: "production_year",
          sortable: false,
        },
        {
          text: "Actors",
          align: "start",
          value: "actors_full_name",
          filterable: false,
          sortable: false,
        },
        {
          text: "Duration",
          align: "start",
          value: "duration",
          filterable: false,
          sortable: false,
        },
        {
          text: "Requester",
          align: "start",
          value: "full_name",
          filterable: false,
          sortable: false,
        },
        {
          text: "Accept",
          align: "center",
          value: "accept",
          filterable: false,
          sortable: false,
        },
        {
          text: "Reject",
          align: "center",
          value: "reject",
          filterable: false,
          sortable: false,
        },
      ];
    },
  },
  methods: {
    showActors() {
      this.showedMovie.actors_full_name.forEach((item) => {
        this.actors += item + ", ";
      });
    },
    openDetailsDialog(item) {
      this.showedMovie = item;
      this.detailsDialog = true;
      this.showActors();
    },
    closeAcceptDialog() {
      this.acceptConfig = {};
      this.acceptDialog = false;
    },
    processActors(item) {
      if (item.actors_full_name == "") item.actors_full_name = [];
      else {
        item.actors_full_name = item.actors_full_name
          .split(",")
          .map((value) => {
            return value.trim();
          });
      }
    },
    processDirectors(item) {
      if (item.directors_full_name == "") item.directors_full_name = [];
      else {
        item.directors_full_name = item.directors_full_name
          .split(",")
          .map((value) => {
            return value.trim();
          });
      }
    },
    processGenre(item) {
      if (item.genres == "") item.genres = [];
      else {
        item.genres = item.genres.split(",").map((value) => {
          return value.trim();
        });
      }
    },
    async deleteMovie(item) {
      try {
        const response = await axiosInstance.delete(URL.REMOVE_MOVIE, {
          data: {
            movieId: item.movie_id,
          },
        });
        this.text = response.data.message;
        this.color = "green lighten-1";
        this.snackbar = true;
        await this.getAllMovies();
      } catch (error) {
        this.text = error.response.data.message;
        this.color = "red darken-1";
        this.snackbar = true;
      }
    },
    async addMovie() {
      if (
        this.add.title === "" ||
        this.add.directors_full_name === "" ||
        this.add.actors_full_name === "" ||
        this.add.production_year === "" ||
        this.add.duration === "" ||
        this.add.genres === "" ||
        this.add.price === "" ||
        this.add.language_option === "" ||
        this.add.subtitle_option === ""
      ) {
        this.text = "All fields must be filled.";
        this.color = "red darken-1";
        this.snackbar = true;
        return;
      }
      this.add.duration = Number(this.add.duration);
      this.add.price = Number(this.add.price);
      if (isNaN(this.add.duration) || isNaN(this.add.price)) {
        this.text = "Field types are not correct.";
        this.color = "red darken-1";
        this.snackbar = true;
        return;
      }
      this.processActors(this.add);
      this.processDirectors(this.add);
      this.processGenre(this.add);
      try {
        const response = await axiosInstance.post(URL.ADD_MOVIE, this.add);
        this.text = response.data.message;
        this.color = "green lighten-1";
        this.snackbar = true;
        await this.getAllMovies();
      } catch (error) {
        this.text = error.response.data.message;
        this.color = "red darken-1";
        this.snackbar = true;
      }
      this.add = {
        title: "",
        directors_full_name: "",
        actors_full_name: "",
        production_year: "",
        duration: "",
        genres: "",
        price: "",
        language_option: "",
        subtitle_option: "",
      };
    },
    async getRequests() {
      try {
        const response = await axiosInstance.get(URL.GET_ALL_REQUESTS);
        this.requests = response.data;
      } catch (error) {
        return;
      }
    },
    async acceptRequest() {
      if (
        this.acceptConfig.price === "" ||
        this.acceptConfig.language_option === "" ||
        this.acceptConfig.subtitle_option === ""
      ) {
        this.text = "All fields must be filled.";
        this.color = "red darken-1";
        this.snackbar = true;
        return;
      }
      this.acceptConfig.price = Number(this.acceptConfig.price);
      if (isNaN(this.acceptConfig.price)) {
        this.text = "Field types are not correct.";
        this.color = "red darken-1";
        this.snackbar = true;
        return;
      }
      this.processActors(this.acceptedMovie);
      this.processDirectors(this.acceptedMovie);
      this.processGenre(this.acceptedMovie);
      try {
        const response = await axiosInstance.post(URL.ADD_MOVIE, {
          ...this.acceptedMovie,
          ...this.acceptConfig,
        });
        this.text = response.data.message;
        this.color = "green lighten-1";
        this.snackbar = true;
        await axiosInstance.delete(URL.REJECT_REQUEST, {
          data: {
            req_movie_id: this.acceptedMovie.req_movie_id,
          },
        });
        await this.getRequests();
        await this.getAllMovies();
      } catch (error) {
        this.text = error.response.data.message;
        this.color = "red darken-1";
        this.snackbar = true;
      }
      this.acceptedMovie = {};
      this.acceptConfig = {};
      this.acceptDialog = false;
    },
    async rejectRequest(item) {
      try {
        const response = await axiosInstance.delete(URL.REJECT_REQUEST, {
          data: {
            req_movie_id: item.req_movie_id,
          },
        });
        this.text = response.data.result;
        this.color = "green lighten-1";
        this.snackbar = true;
        await this.getRequests();
      } catch (error) {
        this.text = error.response.data.result;
        this.color = "red darken-1";
        this.snackbar = true;
      }
    },
    async getAllMovies() {
      try {
        const response = await axiosInstance.get(URL.GET_ALL_MOVIES);
        this.movies = response.data;
      } catch (error) {
        return;
      }
    },
  },
  async created() {
    await this.getAllMovies();
    await this.getRequests();
  },
};
</script>
