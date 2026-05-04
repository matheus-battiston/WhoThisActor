import React from "react";
import ReactDOM from "react-dom/client";
import {
  MutationCache,
  QueryCache,
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import "./index.css";
import App from "./App";
import store, { setError } from "./redux/store";
import { getErrorMessage } from "./utils/getErrorMessage";

const root = ReactDOM.createRoot(document.getElementById("root"));

const handleApiError = (error) => {
  store.dispatch(setError(getErrorMessage(error)));
};

const shouldRetry = (failureCount, error) => {
  const status = error?.response?.status ?? error?.status;

  if (status === 400 || status === 401 || status === 403) {
    return false;
  }

  return failureCount < 3;
};

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: handleApiError,
  }),
  mutationCache: new MutationCache({
    onError: handleApiError,
  }),
  defaultOptions: {
    queries: {
      retry: shouldRetry,
      refetchOnWindowFocus: false,
    },
    mutations: {
      retry: shouldRetry,
    },
  },
});

root.render(
  <QueryClientProvider client={queryClient}>
    <App />
  </QueryClientProvider>,
);
