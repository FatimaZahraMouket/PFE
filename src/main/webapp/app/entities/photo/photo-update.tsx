import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPays } from 'app/shared/model/pays.model';
import { getEntities as getPays } from 'app/entities/pays/pays.reducer';
import { IPlanning } from 'app/shared/model/planning.model';
import { getEntities as getPlannings } from 'app/entities/planning/planning.reducer';
import { IProgramme } from 'app/shared/model/programme.model';
import { getEntities as getProgrammes } from 'app/entities/programme/programme.reducer';
import { IPhoto } from 'app/shared/model/photo.model';
import { getEntity, updateEntity, createEntity, reset } from './photo.reducer';

export const PhotoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pays = useAppSelector(state => state.pays.entities);
  const plannings = useAppSelector(state => state.planning.entities);
  const programmes = useAppSelector(state => state.programme.entities);
  const photoEntity = useAppSelector(state => state.photo.entity);
  const loading = useAppSelector(state => state.photo.loading);
  const updating = useAppSelector(state => state.photo.updating);
  const updateSuccess = useAppSelector(state => state.photo.updateSuccess);

  const handleClose = () => {
    navigate('/photo' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPays({}));
    dispatch(getPlannings({}));
    dispatch(getProgrammes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...photoEntity,
      ...values,
      pays: pays.find(it => it.id.toString() === values.pays.toString()),
      planning: plannings.find(it => it.id.toString() === values.planning.toString()),
      programme: programmes.find(it => it.id.toString() === values.programme.toString()),
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
          ...photoEntity,
          pays: photoEntity?.pays?.id,
          planning: photoEntity?.planning?.id,
          programme: photoEntity?.programme?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="faeApp.photo.home.createOrEditLabel" data-cy="PhotoCreateUpdateHeading">
            <Translate contentKey="faeApp.photo.home.createOrEditLabel">Create or edit a Photo</Translate>
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
                  id="photo-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('faeApp.photo.url')} id="photo-url" name="url" data-cy="url" type="text" />
              <ValidatedField id="photo-pays" name="pays" data-cy="pays" label={translate('faeApp.photo.pays')} type="select">
                <option value="" key="0" />
                {pays
                  ? pays.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="photo-planning"
                name="planning"
                data-cy="planning"
                label={translate('faeApp.photo.planning')}
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
              <ValidatedField
                id="photo-programme"
                name="programme"
                data-cy="programme"
                label={translate('faeApp.photo.programme')}
                type="select"
              >
                <option value="" key="0" />
                {programmes
                  ? programmes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/photo" replace color="info">
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

export default PhotoUpdate;
