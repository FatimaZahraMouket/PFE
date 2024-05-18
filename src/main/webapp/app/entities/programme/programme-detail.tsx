import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './programme.reducer';

export const ProgrammeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const programmeEntity = useAppSelector(state => state.programme.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="programmeDetailsHeading">
          <Translate contentKey="faeApp.programme.detail.title">Programme</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{programmeEntity.id}</dd>
          <dt>
            <span id="heure">
              <Translate contentKey="faeApp.programme.heure">Heure</Translate>
            </span>
          </dt>
          <dd>{programmeEntity.heure}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="faeApp.programme.description">Description</Translate>
            </span>
          </dt>
          <dd>{programmeEntity.description}</dd>
          <dt>
            <Translate contentKey="faeApp.programme.planning">Planning</Translate>
          </dt>
          <dd>{programmeEntity.planning ? programmeEntity.planning.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/programme" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/programme/${programmeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProgrammeDetail;
