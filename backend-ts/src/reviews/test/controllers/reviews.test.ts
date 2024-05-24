import bodyParser from 'body-parser';
import express from 'express';
import sinon from 'sinon';
import chai from 'chai';
import chaiHttp from 'chai-http';
import routers from 'src/reviews/rewiews.router';
import Review from 'src/reviews/reviews.model';
import { ObjectId } from 'mongodb';
import mongoSetup from '../mongoSetup';
import * as fetchFunc from 'src/clients/reviews.restaurants.client';

const { expect } = chai;

chai.use(chaiHttp);
chai.should();

const sandbox = sinon.createSandbox();

const app = express();

app.use(bodyParser.json({ limit: '1mb' }));
app.use('/', routers);
const review1 = new Review({
  _id: new ObjectId(),
  text: "test1",
  date: new Date(),
  phoneNumber: "0999999999",
  restaurantId: 1,
  name: "test1",
  mark: 7,
});
const review2 = new Review({
  _id: new ObjectId(),
  text: "test2",
  date: new Date(),
  phoneNumber: "0999999999",
  restaurantId: 2,
  name: "test2",
  mark: 9,
});
const review3 = new Review({
  _id: new ObjectId(),
  text: "test3",
  date: new Date(),
  phoneNumber: "0999999999",
  restaurantId: 1,
  name: "test3",
  mark: 8,
});

describe('Reviews controller', () => {
  beforeEach(async () => {
    await mongoSetup;
    await review1.save();
    await review2.save();
    await review3.save();
  });
  
  afterEach(() => {
    sandbox.restore();
  });

  it('should save the review', (done) => {
    const  review = {
      "text": "test",
      "phoneNumber": "0999999999",
      "restaurantId": 3,
      "name": "test",
      "mark": 7,
    };

    const fetchStub = sandbox.stub(fetchFunc, 'validateRestaurantId');
    fetchStub.resolves(true);

    chai.request(app)
      .post('/')
      .send(review)
      .end((_, res) => {
        res.should.have.status(201);
        expect(res.body).to.have.property('id');
        done();
      });
  });
  
  it('shouldn\'t save the review with non-existing restaurantId', (done) => {
    const  review = {
      "text": "test",
      "phoneNumber": "0999999999",
      "restaurantId": 50,
    };

    const fetchStub = sandbox.stub(fetchFunc, 'validateRestaurantId');
    fetchStub.rejects(new Error('Fetch response not successful.'));

    chai.request(app)
      .post('/')
      .send(review)
      .end((_, res) => {
        res.should.have.status(500);
        expect(res.body).to.have.property('message').that.equals('Fetch response not successful.');
        done();
      });
  });
  
  it('shouldn\'t save the review with an invalid phoneNumber', (done) => {
    const  review = {
      "text": "test",
      "phoneNumber": "09999999999",
      "restaurantId": 1,
    };
    const fetchStub = sandbox.stub(fetchFunc, 'validateRestaurantId');
    fetchStub.resolves(true);

    chai.request(app)
      .post('/')
      .send(review)
      .end((_, res) => {
        res.should.have.status(500);
        expect(res.body).to.have.property('message').that.equals('Phone number is incorrect.');
        done();
      });
  });

  it('shouldn\'t save the review with text consists of less then 3 characters', (done) => {
    const  review = {
      "text": "t      t    ",
      "phoneNumber": "0999999999",
      "restaurantId": 1,
    };
    const fetchStub = sandbox.stub(fetchFunc, 'validateRestaurantId');
    fetchStub.resolves(true);

    chai.request(app)
      .post('/')
      .send(review)
      .end((_, res) => {
        res.should.have.status(500);
        expect(res.body).to.have.property('message').that.equals('Text of review shouldn\'t be empty and longer then 2 characters.');
        done();
      });
  });
  
  it('shouldn\'t save the review with a mark less than 1 or greater than 10', (done) => {
    const  review = {
      "text": "test",
      "phoneNumber": "0999999999",
      "restaurantId": 1,
      "mark": 0,
    };
    const fetchStub = sandbox.stub(fetchFunc, 'validateRestaurantId');
    fetchStub.resolves(true);

    chai.request(app)
      .post('/')
      .send(review)
      .end((_, res) => {
        res.should.have.status(500);
        expect(res.body).to.have.property('message').that.equals('Mark should be from 1 to 10.');
        done();
      });
  });
  
  it('should list the reviews by resaurantId 1, should return 2 reviews', (done) => {
    chai.request(app)
      .get('/1')
      .end((_, res) => {
        res.should.have.status(200);
        expect(res.body.result.length === 2);
        expect((res.body.result)[0]._id).to.equal(review3._id.toString());
        expect((res.body.result)[0].text).to.equal(review3.text);
        expect((res.body.result)[0].mark).to.equal(review3.mark);
        expect((res.body.result)[1]._id).to.equal(review1._id.toString());
        expect((res.body.result)[1].text).to.equal(review1.text);
        expect((res.body.result)[1].mark).to.equal(review1.mark);
        done();
      });
  });
  
  it('should return a 404 status code when the result is empty', (done) => {
    chai.request(app)
      .get('/50')
      .end((_, res) => {
        res.should.have.status(404);
        expect(res.body._id);
        done();
      });
  });
    
  it('should get the report of counting the reviews by restaurantIds list', (done) => {
    const resultResponse = {1: 2, 2: 1, 3: 1, 50: 0};
    const requestIds = {ids: [1, 2, 3, 50]};
    chai.request(app)
      .post('/_counts')
      .send(requestIds)
      .end((_, res) => {
        res.should.have.status(200);
        expect(res.body).to.deep.equal(resultResponse);
        done();
      });
  });
});
