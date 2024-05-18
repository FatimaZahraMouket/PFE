import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Programme from './programme';
import ProgrammeDetail from './programme-detail';
import ProgrammeUpdate from './programme-update';
import ProgrammeDeleteDialog from './programme-delete-dialog';

const ProgrammeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Programme />} />
    <Route path="new" element={<ProgrammeUpdate />} />
    <Route path=":id">
      <Route index element={<ProgrammeDetail />} />
      <Route path="edit" element={<ProgrammeUpdate />} />
      <Route path="delete" element={<ProgrammeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProgrammeRoutes;
