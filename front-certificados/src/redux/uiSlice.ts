import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

interface UiState {
  pageTitle: string;
}


const initialState: UiState = {
  pageTitle: 'Certify Pro', 
};

const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    setPageTitle: (state, action: PayloadAction<string>) => {
      state.pageTitle = action.payload;
    },

    resetPageTitle: (state) => {
      state.pageTitle = 'Certify Pro';
    }
  }
});

export const { setPageTitle, resetPageTitle } = uiSlice.actions;
export const uiReducer = uiSlice.reducer;
export default uiSlice.reducer;