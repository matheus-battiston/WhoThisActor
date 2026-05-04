export default {
  api: {
    input: "./openapi/openapi.yaml",
    output: {
      target: "./src/api/generated/api.ts",
      client: "react-query",
      override: {
        mutator: {
          path: "./src/api/generated/custom-instance.ts",
          name: "customInstance",
        },
      },
    },
  },
};
