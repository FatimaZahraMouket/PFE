import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Theme from './theme';
import ThemeDetail from './theme-detail';
import ThemeUpdate from './theme-update';
import ThemeDeleteDialog from './theme-delete-dialog';

const ThemeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Theme />} />
    <Route path="new" element={<ThemeUpdate />} />
    <Route path=":id">
      <Route index element={<ThemeDetail />} />
      <Route path="edit" element={<ThemeUpdate />} />
      <Route path="delete" element={<ThemeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ThemeRoutes;
