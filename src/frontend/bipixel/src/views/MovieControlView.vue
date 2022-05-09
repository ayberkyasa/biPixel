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
    <v-row class="align-center mb-2">
      <v-col cols="12" sm="1" class="pl-5"> Select Category: </v-col>
      <v-col cols="12" sm="11">
        <v-chip-group mandatory active-class="primary--text">
          <v-chip
            v-for="el in selectElements"
            :key="el.key"
            @click="selected = el.value"
            >{{ el.value }}</v-chip
          >
        </v-chip-group>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="2"> Select Upper Threshold of Price: </v-col>
      <v-col cols="4">
        <v-text-field
          class="mr-10"
          v-model="price"
          label="Enter a price"
          filled
          rounded
          dense
        ></v-text-field>
      </v-col>
      <v-col cols="2"> Select Lower Threshold of Rate: </v-col>
      <v-col cols="4">
        <v-text-field
          class="mr-10"
          v-model="rate"
          label="Enter a rate between 1 and 10"
          filled
          rounded
          dense
        ></v-text-field>
      </v-col>
    </v-row>
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
          v-model="request.title"
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
          v-model="request.production_year"
          label="Year"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.duration"
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
          v-model="request.language_option"
          label="Language"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.subtitle_option"
          label="Subtitle"
          outlined
          dense
          rounded
          :rules="[rules.required]"
          class="mb-4"
        ></v-text-field>
        <v-text-field
          v-model="request.price"
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
export default {
  data() {
    return {
      request: {
        title: "",
        directors_full_name: [],
        actors_full_name: [],
        production_year: "",
        duration: null,
        genre: [],
      },
      rules: {
        required: (value) => !!value || "Required!",
      },
      showedMovie: {},
      detailsDialog: false,
      search: "",
      price: "",
      rate: "",
      selected: "None",
      movies: [
        {
          title: "Movie 1",
          director: "Director 1",
          genre: "Action",
          rate: "8",
          year: "2019",
          price: "10",
        },
        {
          title: "Movie 2",
          director: "Director 1",
          genre: "Comedy",
          rate: "8.5",
          year: "2018",
          price: "20",
        },
        {
          title: "Movie 3",
          director: "Director 2",
          genre: "Science Fiction",
          rate: "7",
          year: "2019",
          price: "150",
        },
        {
          title: "Movie 4",
          director: "Director 3",
          genre: "Mystery",
          rate: "8",
          year: "2019",
          price: "40",
        },
        {
          title: "Movie 5",
          director: "Director 2",
          genre: "Action",
          rate: "8",
          year: "2021",
          price: "5",
        },
        {
          title: "Movie 6",
          director: "Director 4",
          genre: "Science Fiction",
          rate: "8",
          year: "2019",
          price: "80",
        },
        {
          title: "Movie 7",
          director: "Director 3",
          genre: "Mystery",
          rate: "8",
          year: "2019",
          price: "65",
        },
        {
          title: "Movie 8",
          director: "Director 4",
          genre: "Comedy",
          rate: "8",
          year: "2019",
          price: "110",
        },
      ],
      selectElements: [
        {
          key: "none",
          value: "None",
        },
        {
          key: "action",
          value: "Action",
        },
        {
          key: "comedy",
          value: "Comedy",
        },
        {
          key: "sci_fi",
          value: "Science Fiction",
        },
        {
          key: "romance",
          value: "Romance",
        },
        {
          key: "mystery",
          value: "Mystery",
        },
      ],
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
          value: "genre",
          filter: (value) => {
            if (this.selected === "None") {
              return true;
            } else {
              return value === this.selected;
            }
          },
        },
        {
          text: "Rate",
          align: "start",
          value: "rate",
          sortable: false,
          filter: (value) => {
            if (this.rate === "") {
              return true;
            } else {
              return value >= parseInt(this.rate);
            }
          },
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
          filter: (value) => {
            if (this.price === "") {
              return true;
            } else {
              return value <= parseInt(this.price);
            }
          },
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
  },
  methods: {
    processActors(e) {
      if (e.target.value == "") this.request.actors_full_name = [];
      else {
        this.request.actors_full_name = e.target.value
          .split(",")
          .map((value) => {
            return value.trim();
          });
      }
    },
    processDirectors(e) {
      if (e.target.value == "") this.request.directors_full_name = [];
      else {
        this.request.directors_full_name = e.target.value
          .split(",")
          .map((value) => {
            return value.trim();
          });
      }
    },
    processGenre(e) {
      if (e.target.value == "") this.request.genre = [];
      else {
        this.request.genre = e.target.value.split(",").map((value) => {
          return value.trim();
        });
      }
    },
    addMovie() {
      return;
    },
  },
};
</script>
