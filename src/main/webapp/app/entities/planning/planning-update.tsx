import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOffre } from 'app/shared/model/offre.model';
import { getEntities as getOffres } from 'app/entities/offre/offre.reducer';
import { IPlanning } from 'app/shared/model/planning.model';
import { getEntity, updateEntity, createEntity, reset } from './planning.reducer';

export const PlanningUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const offres = useAppSelector(state => state.offre.entities);
  const planningEntity = useAppSelector(state => state.planning.entity);
  const loading = useAppSelector(state => state.planning.loading);
  const updating = useAppSelector(state => state.planning.updating);
  const updateSuccess = useAppSelector(state => state.planning.updateSuccess);

  const handleClose = () => {
    navigate('/planning' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOffres({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...planningEntity,
      ...values,
      offre: offres.find(it => it.id.toString() === values.offre.toString()),
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
          ...planningEntity,
          offre: planningEntity?.offre?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="faeApp.planning.home.createOrEditLabel" data-cy="PlanningCreateUpdateHeading">
            <Translate contentKey="faeApp.planning.home.createOrEditLabel">Create or edit a Planning</Translate>
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
                  id="planning-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('faeApp.planning.titre')} id="planning-titre" name="titre" data-cy="titre" type="text" />
              <ValidatedField
                label={translate('faeApp.planning.description')}
                id="planning-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('faeApp.planning.jourNumero')}
                id="planning-jourNumero"
                name="jourNumero"
                data-cy="jourNumero"
                type="text"
              />
              <ValidatedField id="planning-offre" name="offre" data-cy="offre" label={translate('faeApp.planning.offre')} type="select">
                <option value="" key="0" />
                {offres
                  ? offres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/planning" replace color="info">
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

export default PlanningUpdate;
