import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPlanning } from 'app/shared/model/planning.model';
import { getEntities as getPlannings } from 'app/entities/planning/planning.reducer';
import { IProgramme } from 'app/shared/model/programme.model';
import { getEntity, updateEntity, createEntity, reset } from './programme.reducer';

export const ProgrammeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const plannings = useAppSelector(state => state.planning.entities);
  const programmeEntity = useAppSelector(state => state.programme.entity);
  const loading = useAppSelector(state => state.programme.loading);
  const updating = useAppSelector(state => state.programme.updating);
  const updateSuccess = useAppSelector(state => state.programme.updateSuccess);

  const handleClose = () => {
    navigate('/programme' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPlannings({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...programmeEntity,
      ...values,
      planning: plannings.find(it => it.id.toString() === values.planning.toString()),
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
          ...programmeEntity,
          planning: programmeEntity?.planning?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="faeApp.programme.home.createOrEditLabel" data-cy="ProgrammeCreateUpdateHeading">
            <Translate contentKey="faeApp.programme.home.createOrEditLabel">Create or edit a Programme</Translate>
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
                  id="programme-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('faeApp.programme.heure')} id="programme-heure" name="heure" data-cy="heure" type="text" />
              <ValidatedField
                label={translate('faeApp.programme.description')}
                id="programme-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="programme-planning"
                name="planning"
                data-cy="planning"
                label={translate('faeApp.programme.planning')}
                type="select"
              >
                <option value="" key="0" />
                {plannings
                  ? plannings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/programme" replace color="info">
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

export default ProgrammeUpdate;
