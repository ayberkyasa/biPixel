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
      item-key="email"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.details`]="{ item }">
        <v-btn
          color="green lighten-1"
          icon
          large
          @click="showedMovie = item"
          @click.stop="detailsDialog = true"
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
          >Directors, Actors, and Genre fields are multivalued so you can enter
          multi values separated with commas in these fields.</span
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
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          @blur="processDirectors"
          label="Directors"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          @blur="processActors"
          label="Actors"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.production_year"
          label="Year"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.duration"
          label="Duration"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          @blur="processGenre"
          label="Genre"
          outlined
          dense
          rounded
          :rules="[rules.required]"
        ></v-text-field>
      </v-col>
      <v-col cols="12" md="4" class="pr-16">
        <v-text-field
          v-model="add.language_option"
          label="Language"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.subtitle_option"
          label="Subtitle"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="add.price"
          label="Price"
          outlined
          dense
          rounded
          :rules="[rules.required]"
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
      item-key="email"
      class="elevation-1"
    >
      <template v-slot:[`item.details`]="{ item }">
        <v-btn
          color="green lighten-1"
          icon
          large
          @click="showedMovie = item"
          @click.stop="detailsDialog = true"
          ><v-icon>mdi-information</v-icon></v-btn
        >
      </template>
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
            <strong>Duration: </strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Language Option: </strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Subtitle Option: </strong>
          </v-row>
          <v-row class="text-subtitle-1">
            <strong>Actors: </strong>
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
            v-model="acceptedMovie.language_option"
            label="Language Option"
            outlined
            dense
            rounded
            class="mb-4"
          ></v-text-field>
          <v-text-field
            v-model="acceptedMovie.subtitle_option"
            label="Subtitle Option"
            outlined
            dense
            rounded
            class="mb-4"
          ></v-text-field>
          <v-text-field
            v-model="acceptedMovie.price"
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
          <v-btn
            color="red darken-1"
            text
            @click="
              acceptDialog = false;
              acceptedMovie.language_option = '';
              acceptedMovie.subtitle_option = '';
              acceptedMovie.price = '';
            "
          >
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
export default {
  data() {
    return {
      requests: [],
      add: {
        title: "",
        directors_full_name: "",
        actors_full_name: "[]",
        production_year: "",
        duration: null,
        genres: "",
      },
      rules: {
        required: (value) => !!value || "Required!",
      },
      acceptedMovie: {},
      acceptDialog: false,
      showedMovie: {},
      detailsDialog: false,
      search: "",
      price: "",
      rate: "",
      selected: "None",
      movies: [],
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
          value: "director",
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
          value: "rate",
          sortable: false,
          filterable: false,
        },
        {
          text: "Year",
          align: "start",
          filterable: false,
          value: "year",
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
          value: "director",
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
          value: "year",
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
    processActors(e) {
      if (e.target.value == "") this.add.actors_full_name = [];
      else {
        this.add.actors_full_name = e.target.value.split(",").map((value) => {
          return value.trim();
        });
      }
    },
    processDirectors(e) {
      if (e.target.value == "") this.add.directors_full_name = [];
      else {
        this.add.directors_full_name = e.target.value
          .split(",")
          .map((value) => {
            return value.trim();
          });
      }
    },
    processGenre(e) {
      if (e.target.value == "") this.add.genres = [];
      else {
        this.add.genres = e.target.value.split(",").map((value) => {
          return value.trim();
        });
      }
    },
    deleteMovie() {},
    addMovie() {
      return;
    },
    acceptRequest() {
      console.log(this.acceptedMovie);
      this.acceptDialog = false;
    },
    rejectRequest(item) {
      console.log(item);
      return;
    },
  },
};
</script>
