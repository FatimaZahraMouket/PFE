import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './planning.reducer';

export const PlanningDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const planningEntity = useAppSelector(state => state.planning.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="planningDetailsHeading">
          <Translate contentKey="faeApp.planning.detail.title">Planning</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{planningEntity.id}</dd>
          <dt>
            <span id="titre">
              <Translate contentKey="faeApp.planning.titre">Titre</Translate>
            </span>
          </dt>
          <dd>{planningEntity.titre}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="faeApp.planning.description">Description</Translate>
            </span>
          </dt>
          <dd>{planningEntity.description}</dd>
          <dt>
            <span id="jourNumero">
              <Translate contentKey="faeApp.planning.jourNumero">Jour Numero</Translate>
            </span>
          </dt>
          <dd>{planningEntity.jourNumero}</dd>
          <dt>
            <Translate contentKey="faeApp.planning.offre">Offre</Translate>
          </dt>
          <dd>{planningEntity.offre ? planningEntity.offre.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/planning" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/planning/${planningEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlanningDetail;
