import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITheme } from 'app/shared/model/theme.model';
import { getEntities as getThemes } from 'app/entities/theme/theme.reducer';
import { IBadge } from 'app/shared/model/badge.model';
import { getEntities as getBadges } from 'app/entities/badge/badge.reducer';
import { IPays } from 'app/shared/model/pays.model';
import { getEntities as getPays } from 'app/entities/pays/pays.reducer';
import { IOffre } from 'app/shared/model/offre.model';
import { Language } from 'app/shared/model/enumerations/language.model';
import { getEntity, updateEntity, createEntity, reset } from './offre.reducer';

export const OffreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const themes = useAppSelector(state => state.theme.entities);
  const badges = useAppSelector(state => state.badge.entities);
  const pays = useAppSelector(state => state.pays.entities);
  const offreEntity = useAppSelector(state => state.offre.entity);
  const loading = useAppSelector(state => state.offre.loading);
  const updating = useAppSelector(state => state.offre.updating);
  const updateSuccess = useAppSelector(state => state.offre.updateSuccess);
  const languageValues = Object.keys(Language);

  const handleClose = () => {
    navigate('/offre' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getThemes({}));
    dispatch(getBadges({}));
    dispatch(getPays({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...offreEntity,
      ...values,
      themes: mapIdList(values.themes),
      badges: mapIdList(values.badges),
      pays: pays.find(it => it.id.toString() === values.pays.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          language: 'FRENCH',
          ...offreEntity,
          themes: offreEntity?.themes?.map(e => e.id.toString()),
          badges: offreEntity?.badges?.map(e => e.id.toString()),
          pays: offreEntity?.pays?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="faeApp.offre.home.createOrEditLabel" data-cy="OffreCreateUpdateHeading">
            <Translate contentKey="faeApp.offre.home.createOrEditLabel">Create or edit a Offre</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="offre-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('faeApp.offre.label')} id="offre-label" name="label" data-cy="label" type="text" />
              <ValidatedField
                label={translate('faeApp.offre.description')}
                id="offre-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('faeApp.offre.prix')} id="offre-prix" name="prix" data-cy="prix" type="text" />
              <ValidatedField label={translate('faeApp.offre.image')} id="offre-image" name="image" data-cy="image" type="text" />
              <ValidatedField
                label={translate('faeApp.offre.latitude')}
                id="offre-latitude"
                name="latitude"
                data-cy="latitude"
                type="text"
              />
              <ValidatedField
                label={translate('faeApp.offre.longitude')}
                id="offre-longitude"
                name="longitude"
                data-cy="longitude"
                type="text"
              />
              <ValidatedField
                label={translate('faeApp.offre.language')}
                id="offre-language"
                name="language"
                data-cy="language"
                type="select"
              >
                {languageValues.map(language => (
                  <option value={language} key={language}>
                    {translate('faeApp.Language.' + language)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('faeApp.offre.themes')}
                id="offre-themes"
                data-cy="themes"
                type="select"
                multiple
                name="themes"
              >
                <option value="" key="0" />
                {themes
                  ? themes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('faeApp.offre.badges')}
                id="offre-badges"
                data-cy="badges"
                type="select"
                multiple
                name="badges"
              >
                <option value="" key="0" />
                {badges
                  ? badges.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="offre-pays" name="pays" data-cy="pays" label={translate('faeApp.offre.pays')} type="select">
                <option value="" key="0" />
                {pays
                  ? pays.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/offre" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OffreUpdate;
