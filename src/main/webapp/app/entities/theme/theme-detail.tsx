import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './theme.reducer';

export const ThemeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const themeEntity = useAppSelector(state => state.theme.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="themeDetailsHeading">
          <Translate contentKey="faeApp.theme.detail.title">Theme</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{themeEntity.id}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="faeApp.theme.label">Label</Translate>
            </span>
          </dt>
          <dd>{themeEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="faeApp.theme.description">Description</Translate>
            </span>
          </dt>
          <dd>{themeEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/theme" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/theme/${themeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ThemeDetail;
