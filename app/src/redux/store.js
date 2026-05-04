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

const dataSlice = createSlice({
  name: "data",
  initialState: {
    data: null,
  },
  reducers: {},
});

const urlSlice = createSlice({
  name: "url",
  initialState: {
    url: null,
  },
  reducers: {
    clearUrl: (state) => {
      state.url = null;
    },
  },
});

const filtroSlice = createSlice({
  name: "filtro",
  initialState: {
    filtro: null,
  },
  reducers: {},
});

const tipoFiltroSlice = createSlice({
  name: "tipoFiltro",
  initialState: {
    tipoFiltro: null,
  },
  reducers: {},
});

const authSlice = createSlice({
  name: "auth",
  initialState: {
    token: localStorage.getItem("token") ?? null,
    usuario: null,
    isAuthenticated: false,
    authChecked: false,
  },
  reducers: {
    clearAuth(state) {
      localStorage.removeItem("token");
      state.token = null;
      state.usuario = null;
      state.isAuthenticated = false;
      state.authChecked = true;
    },
    setToken(state, action) {
      localStorage.setItem("token", action.payload);
      state.token = action.payload;
    },
    setUsuario(state, action) {
      state.usuario = action.payload;
    },
    setIsAuthenticated(state, action) {
      state.isAuthenticated = action.payload;
    },
    setAuthChecked(state, action) {
      state.authChecked = action.payload;
    },
  },
});

export const { setError, clearError } = errorSlice.actions;
export const {
  clearAuth,
  setToken,
  setUsuario,
  setIsAuthenticated,
  setAuthChecked,
} = authSlice.actions;

export const store = configureStore({
  reducer: {
    auth: authSlice.reducer,
    error: errorSlice.reducer,
    data: dataSlice.reducer,
    url: urlSlice.reducer,
    filtro: filtroSlice.reducer,
    tipoFiltro: tipoFiltroSlice.reducer,
  },
});

export default store;
