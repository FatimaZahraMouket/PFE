import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './badge.reducer';

export const BadgeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const badgeEntity = useAppSelector(state => state.badge.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="badgeDetailsHeading">
          <Translate contentKey="faeApp.badge.detail.title">Badge</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{badgeEntity.id}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="faeApp.badge.label">Label</Translate>
            </span>
          </dt>
          <dd>{badgeEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="faeApp.badge.description">Description</Translate>
            </span>
          </dt>
          <dd>{badgeEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/badge" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/badge/${badgeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BadgeDetail;
