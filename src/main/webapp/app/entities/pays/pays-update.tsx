import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPays } from 'app/shared/model/pays.model';
import { getEntity, updateEntity, createEntity, reset } from './pays.reducer';

export const PaysUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paysEntity = useAppSelector(state => state.pays.entity);
  const loading = useAppSelector(state => state.pays.loading);
  const updating = useAppSelector(state => state.pays.updating);
  const updateSuccess = useAppSelector(state => state.pays.updateSuccess);

  const handleClose = () => {
    navigate('/pays' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...paysEntity,
      ...values,
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
          ...paysEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="faeApp.pays.home.createOrEditLabel" data-cy="PaysCreateUpdateHeading">
            <Translate contentKey="faeApp.pays.home.createOrEditLabel">Create or edit a Pays</Translate>
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
                  id="pays-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('faeApp.pays.label')} id="pays-label" name="label" data-cy="label" type="text" />
              <ValidatedField
                label={translate('faeApp.pays.description')}
                id="pays-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('faeApp.pays.reviews')} id="pays-reviews" name="reviews" data-cy="reviews" type="text" />
              <ValidatedField
                label={translate('faeApp.pays.continent')}
                id="pays-continent"
                name="continent"
                data-cy="continent"
                type="text"
              />
              <ValidatedField label={translate('faeApp.pays.latitude')} id="pays-latitude" name="latitude" data-cy="latitude" type="text" />
              <ValidatedField
                label={translate('faeApp.pays.longitude')}
                id="pays-longitude"
                name="longitude"
                data-cy="longitude"
                type="text"
              />
              <ValidatedField
                label={translate('faeApp.pays.isSaved')}
                id="pays-isSaved"
                name="isSaved"
                data-cy="isSaved"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('faeApp.pays.isLiked')}
                id="pays-isLiked"
                name="isLiked"
                data-cy="isLiked"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pays" replace color="info">
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

export default PaysUpdate;
