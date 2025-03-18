// store.js
import { configureStore, createSlice } from "@reduxjs/toolkit";

const errorSlice = createSlice({
  name: "error",
  initialState: {
    hasError: false,
    message: "",
  },
  reducers: {
    setError: (state, action) => {
      state.hasError = true;
      state.message = action.payload;
    },
    clearError: (state) => {
      state.hasError = false;
      state.message = "";
    },
  },
});

const fastSlice = createSlice({
  name: "fast",
  initialState: {
    isFast: false,
  },
  reducers: {
    setFast: (state, action) => {
      state.fast = action.payload;
    },
    clearFast: (state) => {
      state.fast = false;
    },
  },
});

const dataSlice = createSlice({
  name: "data",
  initialState: {
    data: null,
  },
  reducers: {
    setData: (state, action) => {
      state.data = action.payload;
    },
    clearData: (state) => {
      state.data = null;
    },
  },
});

const urlSlice = createSlice({
  name: "url",
  initialState: {
    data: null,
  },
  reducers: {
    setUrl: (state, action) => {
      state.url = action.payload;
    },
    clearUrl: (state) => {
      state.url = null;
    },
  },
});

export const { setError, clearError } = errorSlice.actions;
export const { setData, clearData } = dataSlice.actions;
export const { setUrl, clearUrl } = urlSlice.actions;
export const { setFast, clearFast } = fastSlice.actions;

export const store = configureStore({
  reducer: {
    error: errorSlice.reducer,
    data: dataSlice.reducer,
    url: urlSlice.reducer,
    fast: fastSlice.reducer,
  },
});
