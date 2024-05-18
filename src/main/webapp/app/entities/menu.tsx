import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/pays">
        <Translate contentKey="global.menu.entities.pays" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/theme">
        <Translate contentKey="global.menu.entities.theme" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/badge">
        <Translate contentKey="global.menu.entities.badge" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/offre">
        <Translate contentKey="global.menu.entities.offre" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/planning">
        <Translate contentKey="global.menu.entities.planning" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/programme">
        <Translate contentKey="global.menu.entities.programme" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/photo">
        <Translate contentKey="global.menu.entities.photo" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
